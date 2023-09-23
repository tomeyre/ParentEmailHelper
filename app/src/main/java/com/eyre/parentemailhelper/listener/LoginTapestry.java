package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.eyre.parentemailhelper.activity.Credentials;
import com.eyre.parentemailhelper.asyncTask.TapestryCredentials;

public class LoginTapestry implements View.OnClickListener {

    private Context context;
    private String username;
    private String password;

    public LoginTapestry(Context context, String username, String password){
        this.context = context;
        this.username = username;
        this.password = password;
    }

    @Override
    public void onClick(View arg0) {
        new TapestryCredentials().check(username,password);
    }
}