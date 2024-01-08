package com.eyre.parentemailhelper.activity;

import static com.eyre.parentemailhelper.util.KeyStoreHelper.retrieveUsername;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.CheckCredentialsTapestryListener;
import com.eyre.parentemailhelper.listener.TapestryDeleteCalendarListener;
import com.eyre.parentemailhelper.listener.TapestryEventsListener;
import com.eyre.parentemailhelper.pojo.ParentMailSession;
import com.eyre.parentemailhelper.schedule.CheckNewEmailsJob;
import com.eyre.parentemailhelper.util.RequestParentMail;
import com.eyre.parentemailhelper.util.RequestTapestry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final int DOWNLOAD_JOB_KEY = 101;
    boolean loadingFinished = true;
    boolean redirect = false;

    private Button provideLoginCredentials;
    private Button checkTapestryForNewEvents;
    private Button deleteTapestryCalendar;
    private CheckCredentialsTapestryListener checkCredentialsTapestryListener;
    private TapestryEventsListener tapestryEventsListener;
    private TapestryDeleteCalendarListener tapestryDeleteCalendarListener;

    private ProgressBar progressBar;
    private TextView progressBarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        provideLoginCredentials = findViewById(R.id.provideLoginCredentials);
        checkTapestryForNewEvents = findViewById(R.id.checkTapestryForNewEvents);
        deleteTapestryCalendar = findViewById(R.id.deleteTapestryCalendar);
        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.progressBarText);

        checkCredentialsTapestryListener = new CheckCredentialsTapestryListener(this);
        tapestryEventsListener = new TapestryEventsListener(this, progressBar, progressBarText);
        tapestryDeleteCalendarListener = new TapestryDeleteCalendarListener(this);

        provideLoginCredentials.setOnClickListener(checkCredentialsTapestryListener);
        checkTapestryForNewEvents.setOnClickListener(tapestryEventsListener);
        deleteTapestryCalendar.setOnClickListener(tapestryDeleteCalendarListener);

        initJob();
    }

    private void initJob() {
        ComponentName componentName = new ComponentName(this, CheckNewEmailsJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(DOWNLOAD_JOB_KEY, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true);
        builder.setPeriodic(18 * 60 * 60 * 1000, 24 * 60 * 60 * 1000);

        JobScheduler checkNewEmailsJob = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        checkNewEmailsJob.schedule(builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (retrieveUsername(this) != null && !retrieveUsername(this).isEmpty()) {
            provideLoginCredentials.setText("Change Tapestry Login Details");
            checkTapestryForNewEvents.setVisibility(View.VISIBLE);
            deleteTapestryCalendar.setVisibility(View.VISIBLE);
        }
    }
}