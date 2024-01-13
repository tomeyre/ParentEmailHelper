package com.eyre.parentemailhelper.activity;

import static com.eyre.parentemailhelper.util.KeyStoreHelper.retrieveUsername;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.CheckCredentialsParentMailListener;
import com.eyre.parentemailhelper.listener.CheckCredentialsTapestryListener;
import com.eyre.parentemailhelper.listener.DeleteCalendarListener;
import com.eyre.parentemailhelper.listener.ParentMailEventsListener;
import com.eyre.parentemailhelper.listener.TapestryEventsListener;
import com.eyre.parentemailhelper.schedule.CheckNewEmailsJob;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final int DOWNLOAD_JOB_KEY = 101;
    boolean loadingFinished = true;
    boolean redirect = false;

    private final Integer DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

    private Button provideLoginCredentialsTapestry;
    private Button provideLoginCredentialsParentMail;
    private Button checkTapestryForNewEvents;
    private Button checkParentMailForNewEvents;
    private Button deleteCalendar;
    private CheckCredentialsTapestryListener checkCredentialsTapestryListener;
    private CheckCredentialsParentMailListener checkCredentialsParentMailListener;
    private TapestryEventsListener tapestryEventsListener;
    private ParentMailEventsListener parentMailEventsListener;
    private DeleteCalendarListener deleteCalendarListener;

    private ProgressBar progressBar;
    private TextView progressBarText;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------TAPESTRY
        provideLoginCredentialsTapestry = findViewById(R.id.provideLoginCredentialsTapestry);
        checkCredentialsTapestryListener = new CheckCredentialsTapestryListener(this);
        provideLoginCredentialsTapestry.setOnClickListener(checkCredentialsTapestryListener);

        checkTapestryForNewEvents = findViewById(R.id.checkTapestryForNewEvents);
        tapestryEventsListener = new TapestryEventsListener(this, progressBar, progressBarText);
        checkTapestryForNewEvents.setOnClickListener(tapestryEventsListener);

        //--------PARENT MAIL
        provideLoginCredentialsParentMail = findViewById(R.id.provideLoginCredentialsParentMail);
        checkCredentialsParentMailListener = new CheckCredentialsParentMailListener(this);
        provideLoginCredentialsParentMail.setOnClickListener(checkCredentialsParentMailListener);

        webView = findViewById(R.id.webView);
        checkParentMailForNewEvents = findViewById(R.id.checkParentMailForNewEvents);
        parentMailEventsListener = new ParentMailEventsListener(this, progressBar, progressBarText, webView);
        checkParentMailForNewEvents.setOnClickListener(parentMailEventsListener);

        //--------GENERIC
        deleteCalendar = findViewById(R.id.deleteCalendar);
        deleteCalendarListener = new DeleteCalendarListener(this);
        deleteCalendar.setOnClickListener(deleteCalendarListener);


        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);

//        initJob();
//        setNewEmailCheckOn();
    }


//    private void initJob() {
//        ComponentName componentName = new ComponentName(this, CheckNewEmailsJob.class);
//        JobInfo.Builder builder = new JobInfo.Builder(DOWNLOAD_JOB_KEY, componentName)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                .setPersisted(true);
//        builder.setPeriodic(60 * 1000, 60 * 1000);
//
//        JobScheduler checkNewEmailsJob = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        checkNewEmailsJob.schedule(builder.build());
//
//        for(JobInfo job : checkNewEmailsJob.getAllPendingJobs()){
//            System.out.println("Job ID: " + job.getId() +" || name: " + job);
//        }
//    }

    public void setNewEmailCheckOn() {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        PendingIntent operation =
                new CheckNewEmailsJob(this).getReminderPendingIntent();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (Build.VERSION.SDK_INT >= 23) {

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, operation);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  60 * 1000, operation);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (retrieveUsername(this) != null && !retrieveUsername(this).isEmpty()) {
            provideLoginCredentialsTapestry.setText("Change Tapestry Login Details");
            checkTapestryForNewEvents.setVisibility(View.VISIBLE);
            deleteCalendar.setVisibility(View.VISIBLE);
        }
    }
}