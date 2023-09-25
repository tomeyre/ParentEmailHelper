package com.eyre.parentemailhelper.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.LoginTapestryListener;

public class CredentialsCheckingActivity extends AppCompatActivity {

    Button loginTapestryButton;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credentials);

        loginTapestryButton = findViewById(R.id.loginTapestry);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginTapestryButton.setOnClickListener(new LoginTapestryListener(this));
    }

    public String getUsername(){
        return username.getText().toString().trim();
    }

    public String getPassword(){
        return password.getText().toString().trim();
    }
}
