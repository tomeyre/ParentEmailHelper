package com.eyre.parentemailhelper.schedule;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import com.eyre.parentemailhelper.asyncTask.LookForNewTapestryEventsBackgroundService;

public class CheckNewEmailsJob extends JobService {

    private Context context;
    private LookForNewTapestryEventsBackgroundService lookForNewTapestryEventsBackgroundService;

    public CheckNewEmailsJob(){}

    public CheckNewEmailsJob(Context context){
        this.context = context;
    }
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        lookForNewTapestryEventsBackgroundService = new LookForNewTapestryEventsBackgroundService();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
