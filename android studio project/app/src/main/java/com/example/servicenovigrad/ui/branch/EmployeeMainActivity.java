package com.example.servicenovigrad.ui.branch;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.ui.WelcomeActivity;

public class EmployeeMainActivity extends WelcomeActivity {
    private Button requestsButton;
    private BranchAccount user;
    private String reqButtonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        welcomeText = findViewById(R.id.employeeWelcomeText);

        Button profileButton = findViewById(R.id.branchProfileButton);
        profileButton.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));

        requestsButton = findViewById(R.id.branchServiceButton);
        requestsButton.setOnClickListener(v -> startActivity(new Intent(this, BranchServiceManage.class)));
        reqButtonText = (String) requestsButton.getText();

        user = (BranchAccount) DatabaseHandler.user;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            requestsButton.setEnabled(user.getRequests().size() != 0);
            String str = reqButtonText + " (" + user.getRequests().size() + ")";
            requestsButton.setText(str);
        }
    }
}