package com.eyre.parentemailhelper.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.LoginTapestryListener;

public class TapestryCredentialsCheckingActivity extends AppCompatActivity {

    Button loginTapestryButton;
    EditText username;
    EditText password;

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credentials);

        loginTapestryButton = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        title = findViewById(R.id.title);
        title.setText("Login to Tapestry");

        loginTapestryButton.setOnClickListener(new LoginTapestryListener(this));
    }

    public String getTapestryUsername(){
        return username.getText().toString().trim();
    }

    public String getTapestryPassword(){
        return password.getText().toString().trim();
    }
}
