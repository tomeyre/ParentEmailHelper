package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.eyre.parentemailhelper.asyncTask.CheckParentMailCredentialsAreValidBackgroundService;

public class LoginParentMailListener implements View.OnClickListener {

    private Context context;
    private WebView webView;

    public LoginParentMailListener(Context context, WebView webView){
        this.context = context;
        this.webView = webView;
    }

    @Override
    public void onClick(View arg0) {
        new CheckParentMailCredentialsAreValidBackgroundService().check(context, webView);
    }
}