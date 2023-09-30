package com.eyre.parentemailhelper.activity;

import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_CREDENTIALS;
import static com.eyre.parentemailhelper.util.InternalStorage.read;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.TapestryDeleteCalendarListener;
import com.eyre.parentemailhelper.listener.TapestryEventsListener;
import com.eyre.parentemailhelper.listener.CheckCredentialsTapestryListener;

public class MainActivity extends AppCompatActivity {

    Button provideLoginCredentials;
    Button checkTapestryForNewEvents;
    Button deleteTapestryCalendar;
    CheckCredentialsTapestryListener checkCredentialsTapestryListener;
    TapestryEventsListener tapestryEventsListener;
    TapestryDeleteCalendarListener tapestryDeleteCalendarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkCredentialsTapestryListener = new CheckCredentialsTapestryListener(this);
        tapestryEventsListener = new TapestryEventsListener(this);
        tapestryDeleteCalendarListener = new TapestryDeleteCalendarListener(this);

        provideLoginCredentials = findViewById(R.id.provideLoginCredentials);
        checkTapestryForNewEvents = findViewById(R.id.checkTapestryForNewEvents);
        deleteTapestryCalendar = findViewById(R.id.deleteTapestryCalendar);

        provideLoginCredentials.setOnClickListener(checkCredentialsTapestryListener);
        checkTapestryForNewEvents.setOnClickListener(tapestryEventsListener);
        deleteTapestryCalendar.setOnClickListener(tapestryDeleteCalendarListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String creds = read(this, TAPESTRY_CREDENTIALS, TAPESTRY);
        if (!creds.isEmpty()) {
            provideLoginCredentials.setText("Change Tapestry Login Details");
            checkTapestryForNewEvents.setVisibility(View.VISIBLE);
            deleteTapestryCalendar.setVisibility(View.VISIBLE);
        }
    }

}