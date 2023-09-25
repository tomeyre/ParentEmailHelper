package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.view.View;

import com.eyre.parentemailhelper.activity.CredentialsCheckingActivity;
import com.eyre.parentemailhelper.asyncTask.CheckTapestryCredentialsAreValidBackgroundService;

public class LoginTapestryListener implements View.OnClickListener {

    private Context context;
    public LoginTapestryListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        new CheckTapestryCredentialsAreValidBackgroundService().check(((CredentialsCheckingActivity)context).getUsername() ,((CredentialsCheckingActivity)context).getPassword(), context);
    }
}