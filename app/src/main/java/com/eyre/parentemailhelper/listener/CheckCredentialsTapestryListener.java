package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.eyre.parentemailhelper.activity.CredentialsCheckingActivity;

public class CheckCredentialsTapestryListener implements View.OnClickListener {

    private Context context;

    public CheckCredentialsTapestryListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        Intent myIntent = new Intent(context, CredentialsCheckingActivity.class);
        context.startActivity(myIntent);

    }
}