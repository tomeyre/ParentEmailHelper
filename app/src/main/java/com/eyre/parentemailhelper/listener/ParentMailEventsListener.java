package com.eyre.parentemailhelper.listener;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eyre.parentemailhelper.asyncTask.LookForNewParentMailEventsBackgroundService;

public class ParentMailEventsListener implements View.OnClickListener {

    private Context context;
    private ProgressBar progressBar;
    private TextView progressBarText;
    private WebView webView;

    public ParentMailEventsListener(Context context, ProgressBar progressBar, TextView progressBarText, WebView webView){
        this.context = context;
        this.progressBar = progressBar;
        this.progressBarText = progressBarText;
        this.webView = webView;
    }

    @Override
    public void onClick(View arg0) {
//        getCalendarId(context, TAPESTRY);
        new LookForNewParentMailEventsBackgroundService().getNewParentMailEvents(context, progressBar, progressBarText, webView);
    }
}