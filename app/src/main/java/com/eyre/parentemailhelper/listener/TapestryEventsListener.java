package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eyre.parentemailhelper.asyncTask.LookForNewTapestryEventsBackgroundService;

public class TapestryEventsListener implements View.OnClickListener {

    private Context context;
    private ProgressBar progressBar;
    private TextView progressBarText;

    public TapestryEventsListener(Context context, ProgressBar progressBar, TextView progressBarText){
        this.context = context;
        this.progressBar = progressBar;
        this.progressBarText = progressBarText;
    }

    @Override
    public void onClick(View arg0) {
//        getCalendarId(context, TAPESTRY);
        new LookForNewTapestryEventsBackgroundService().getNewTapestryEvents(context, progressBar, progressBarText);
    }
}