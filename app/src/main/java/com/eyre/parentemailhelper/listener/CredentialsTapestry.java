package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.eyre.parentemailhelper.activity.Credentials;

public class CredentialsTapestry implements View.OnClickListener {

    private Context context;

    public CredentialsTapestry(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        Intent myIntent = new Intent(context, Credentials.class);
        context.startActivity(myIntent);

    }
}