package com.eyre.parentemailhelper.asyncTask;

import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_BASE_PATH;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_CREDENTIALS;
import static com.eyre.parentemailhelper.util.DisplayMessage.displayLong;
import static com.eyre.parentemailhelper.util.InternalStorage.write;

import android.content.Context;

import com.eyre.parentemailhelper.pojo.TapestryLoginCredentials;
import com.eyre.parentemailhelper.util.Request;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckTapestryCredentialsAreValidBackgroundService {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private String error = "";

    public void check(String username, String password, Context context) {

        executor.execute(new Runnable() {
             @Override
             public void run() {
                 //get initial cookie
                 String json = new Request().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/");

                 // get _token using jsoup
                 Document doc = Jsoup.parse(json);

                 String token = "";
                 for(Element element : doc.getElementsByTag("input")){
                     if(element.attr("name").equals("_token")){
                         token = element.attr("value");
                     }
                 }

                 HashMap<String, String> params = new HashMap<>();
                 TapestryLoginCredentials tapestryLoginCredentials = new TapestryLoginCredentials();
                 tapestryLoginCredentials.setUsername(username);
                 tapestryLoginCredentials.setPassword(password);
                 params.put("email",username);
                 params.put("password",password);
                 params.put("login_redirect_url","");
                 params.put("login_redirect_school","");
                 params.put("oauth","");
                 params.put("oauth_login_url","");
                 params.put("_token",token);
                 //login
                 if(!username.isEmpty() && !password.isEmpty()) {
                     json = new Request().getJSONFromUrlUsingPost(TAPESTRY_BASE_PATH + "/login", params);
                     doc = Jsoup.parse(json);
                     if(doc.getElementsByClass("alert-danger").size() > 0){
                         for(Element element : doc.getElementsByClass("alert-danger")) {
                             error += element.select("div").text().substring(0, element.select("div").text().indexOf(".") + 1).toString() + "\n";
                         }
                         displayLong(error, context);
                     } else {
                         try {
                             ObjectMapper objectMapper = new ObjectMapper();
                             write(context, TAPESTRY_CREDENTIALS, objectMapper.writeValueAsString(tapestryLoginCredentials), TAPESTRY);
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                     }
                 }
             }
         });
    }
}
