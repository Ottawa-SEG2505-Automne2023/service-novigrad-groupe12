package com.example.servicenovigrad.ui;

import android.os.Bundle;

import com.example.servicenovigrad.R;

public class MainActivity extends WelcomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.mainWelcomeText);
    }
}