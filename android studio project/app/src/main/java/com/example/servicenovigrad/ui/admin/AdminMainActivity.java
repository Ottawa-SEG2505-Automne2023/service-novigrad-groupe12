package com.example.servicenovigrad.ui.admin;


import android.content.Intent;
import android.os.Bundle;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.ui.WelcomeActivity;

public class AdminMainActivity extends WelcomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Simple layout with two buttons and a welcome text
        welcomeText = findViewById(R.id.adminWelcomeText);
        findViewById(R.id.adminHandleUsersButton).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AdminAccountManageActivity.class)));
        findViewById(R.id.adminHandleServicesButton).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AdminServiceManageActivity.class)));
    }
}