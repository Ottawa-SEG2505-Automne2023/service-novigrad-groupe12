package com.example.servicenovigrad.ui.branch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.ui.WelcomeActivity;

public class EmployeeMainActivity extends WelcomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        welcomeText = findViewById(R.id.employeeWelcomeText);

        Button profileButton = findViewById(R.id.branchProfileButton);
        profileButton.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
    }
}