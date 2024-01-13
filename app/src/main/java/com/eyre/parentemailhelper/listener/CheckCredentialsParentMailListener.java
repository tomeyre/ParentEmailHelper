package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.eyre.parentemailhelper.activity.ParentMailCredentialsCheckingActivity;

public class CheckCredentialsParentMailListener implements View.OnClickListener {

    private Context context;

    public CheckCredentialsParentMailListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        Intent myIntent = new Intent(context, ParentMailCredentialsCheckingActivity.class);
        context.startActivity(myIntent);

    }
}