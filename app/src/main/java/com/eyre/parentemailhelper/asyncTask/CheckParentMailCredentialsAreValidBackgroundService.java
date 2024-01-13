package com.eyre.parentemailhelper.asyncTask;

import static com.eyre.parentemailhelper.util.DisplayMessage.displayLong;
import static com.eyre.parentemailhelper.util.DisplayMessage.displayShort;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.eyre.parentemailhelper.activity.ParentMailCredentialsCheckingActivity;

public class CheckParentMailCredentialsAreValidBackgroundService {

    public void check(Context context, WebView webView) {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().contains("https://pmx.parentmail.co.uk/api/v1.9/feed?")) {
                    generateAndStoreSymmetricKey(context);
                    displayLong("Logged in successfully", context);
                    ((ParentMailCredentialsCheckingActivity)context).finish();
                }
                return null;
            }
        });
        displayShort("Checking Username", context);
        webView.loadUrl("https://pmx.parentmail.co.uk/#core/login");
        webView.postDelayed(() -> {
            // Call JavaScript to fill the username and password fields and click the login button
            webView.loadUrl("javascript:" +
                    "var usernameField = document.getElementById('username');" +
                    "var continueButton = document.getElementById('continueButton');" +
                    "if (usernameField && continueButton) {" +
                    "  usernameField.value = '" + ((ParentMailCredentialsCheckingActivity) context).getParentMailUsername() + "';" +
                    "  continueButton.click();" +
                    "}"); // Adjust the delay as needed
        }, 5000); // Adjust the delay as needed

        webView.postDelayed(() -> {
            // Call JavaScript to fill the username and password fields and click the login button
            webView.loadUrl("javascript:document.querySelector('input[type=\"submit\"]').click()"); // Adjust the delay as needed
        }, 10000); // Adjust the delay as needed

        displayShort("Checking Password", context);
        webView.postDelayed(() -> {
            // Call JavaScript to fill the username and password fields and click the login button
            webView.loadUrl("javascript:(function(){" +
                    "  var inputField = document.getElementById('input52');" +
                    "  if (inputField) {" +
                    "    inputField.focus();" +
                    "    inputField.value = '" + ((ParentMailCredentialsCheckingActivity) context).getParentMailPassword() + "';" +
                    "    inputField.dispatchEvent(new Event('input', { bubbles: true }));" +
                    "    inputField.dispatchEvent(new Event('change', { bubbles: true }));" +
                    "  }" +
                    "})()"); // Adjust the delay as needed
        }, 15000); // Adjust the delay as needed

        webView.postDelayed(() -> {
            // Call JavaScript to fill the username and password fields and click the login button
            webView.loadUrl("javascript:document.querySelector('input[type=\"submit\"]').click()"); // Adjust the delay as needed
        }, 16000); // Adjust the delay as needed
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
                    "pmpas",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Store the encrypted key in the SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("pmun", ((ParentMailCredentialsCheckingActivity) context).getParentMailUsername());
            editor.putString("pmpd", ((ParentMailCredentialsCheckingActivity) context).getParentMailPassword());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
