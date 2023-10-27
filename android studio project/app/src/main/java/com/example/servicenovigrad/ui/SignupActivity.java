package com.example.servicenovigrad.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.Updatable;

public class SignupActivity extends AppCompatActivity implements Updatable {
    private Spinner roleSpinner;
    private EditText signupUsername, signupPrenom, signupNom, signupPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        roleSpinner = (Spinner) findViewById(R.id.roleSpinner);
        signupUsername = (EditText) findViewById(R.id.signupUsername);
        signupPrenom = (EditText) findViewById(R.id.signupPrenom);
        signupNom = (EditText) findViewById(R.id.signupNom);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        btnSignup = (Button) findViewById(R.id.btnSignup);
    }

    // Todo: Add TextViews to explain things; validate username & password with FieldValidator, update button accordingly.
    public void update() {

    }
}