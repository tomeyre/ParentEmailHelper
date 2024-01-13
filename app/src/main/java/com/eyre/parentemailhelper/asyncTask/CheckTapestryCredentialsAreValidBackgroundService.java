package com.eyre.parentemailhelper.asyncTask;

import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_BASE_PATH;
import static com.eyre.parentemailhelper.util.DisplayMessage.displayLong;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.eyre.parentemailhelper.activity.TapestryCredentialsCheckingActivity;
import com.eyre.parentemailhelper.util.RequestTapestry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckTapestryCredentialsAreValidBackgroundService {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private String error = "";

    public void check(Context context) {

        executor.execute(new Runnable() {
             @Override
             public void run() {
                 //get initial cookie
                 String json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/");

                 // get _token using jsoup
                 Document doc = Jsoup.parse(json);

                 String token = "";
                 for(Element element : doc.getElementsByTag("input")){
                     if(element.attr("name").equals("_token")){
                         token = element.attr("value");
                     }
                 }

                 HashMap<String, String> params = new HashMap<>();
                 params.put("email", ((TapestryCredentialsCheckingActivity)context).getTapestryUsername());
                 params.put("password", ((TapestryCredentialsCheckingActivity)context).getTapestryPassword());
                 params.put("login_redirect_url","");
                 params.put("login_redirect_school","");
                 params.put("oauth","");
                 params.put("oauth_login_url","");
                 params.put("_token",token);
                 //login
                 if(!params.get("email").isEmpty() && !params.get("password").isEmpty()) {
                     json = new RequestTapestry().getJSONFromUrlUsingPost(TAPESTRY_BASE_PATH + "/login", params);
                     doc = Jsoup.parse(json);
                     if(doc.getElementsByClass("alert-danger").size() > 0){
                         for(Element element : doc.getElementsByClass("alert-danger")) {
                             error += element.select("div").text().substring(0, element.select("div").text().indexOf(".") + 1).toString() + "\n";
                         }
                         displayLong(error, context);
                     } else {
                         try {
                             generateAndStoreSymmetricKey(context);
                             displayLong("Logged in successfully", context);
                             ((TapestryCredentialsCheckingActivity)context).finish();
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                     }
                 }
             }
         });
    }

    private static void generateAndStoreSymmetricKey(Context context) {
        try {
            // Generate a master key for encrypting SharedPreferences
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Create an instance of EncryptedSharedPreferences
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "tappas",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Store the encrypted key in the SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("tapun", ((TapestryCredentialsCheckingActivity)context).getTapestryUsername());
            editor.putString("tappd", ((TapestryCredentialsCheckingActivity)context).getTapestryPassword());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
