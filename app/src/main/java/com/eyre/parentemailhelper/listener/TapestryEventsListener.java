package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.view.View;

import com.eyre.parentemailhelper.asyncTask.LookForNewTapestryEventsBackgroundService;

public class TapestryEventsListener implements View.OnClickListener {

    private Context context;

    public TapestryEventsListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        new LookForNewTapestryEventsBackgroundService().getNewTapestryEvents(context);
    }
}