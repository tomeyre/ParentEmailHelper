package com.eyre.parentemailhelper.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.CredentialsTapestry;

public class MainActivity extends AppCompatActivity {

    Button tapestryButton;
    CredentialsTapestry credentialsTapestry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        credentialsTapestry = new CredentialsTapestry(this);

        tapestryButton = findViewById(R.id.tapestry);
        tapestryButton.setOnClickListener(credentialsTapestry);
    }
}