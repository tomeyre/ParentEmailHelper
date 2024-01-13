package com.eyre.parentemailhelper.schedule;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.eyre.parentemailhelper.asyncTask.LookForNewTapestryEventsBackgroundService;

public class CheckNewEmailsJob extends IntentService {
    private static final String TAG = CheckNewEmailsJob.class.getSimpleName();

    LookForNewTapestryEventsBackgroundService lookForNewTapestryEventsBackgroundService;

    private Context context;

    public PendingIntent getReminderPendingIntent() {
        Intent action = new Intent(context, CheckNewEmailsJob.class);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public CheckNewEmailsJob() {
        super(TAG);
    }

    public CheckNewEmailsJob(Context context) {
        super(TAG);
        this.context = context;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        lookForNewTapestryEventsBackgroundService = new LookForNewTapestryEventsBackgroundService();
        lookForNewTapestryEventsBackgroundService.getNewTapestryEvents(context);

    }
}