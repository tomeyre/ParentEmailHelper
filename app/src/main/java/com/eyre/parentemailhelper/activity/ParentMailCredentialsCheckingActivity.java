package com.eyre.parentemailhelper.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eyre.parentemailhelper.R;
import com.eyre.parentemailhelper.listener.LoginParentMailListener;

public class ParentMailCredentialsCheckingActivity extends AppCompatActivity {

    Button loginParentMailButton;
    EditText username;
    EditText password;

    TextView title;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credentials);

        loginParentMailButton = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        title = findViewById(R.id.title);
        title.setText("Login to Parent Mail");

        webView = findViewById(R.id.webView);

        loginParentMailButton.setOnClickListener(new LoginParentMailListener(this, webView));
    }

    public String getParentMailUsername(){
        return username.getText().toString().trim();
    }

    public String getParentMailPassword(){
        return password.getText().toString().trim();
    }
}
