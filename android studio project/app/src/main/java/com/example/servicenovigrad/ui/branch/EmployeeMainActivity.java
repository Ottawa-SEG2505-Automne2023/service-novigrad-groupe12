package com.example.servicenovigrad.ui.branch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.Account;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.backend.services.FilledForm;
import com.example.servicenovigrad.ui.WelcomeActivity;

import java.util.ArrayList;

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

        // TO DELETE ONCE DELIVERABLE 4 IS UNDER WAY
        if (user.getRequests() != null) {
            if (user.getRequests().size() == 0) {
                FilledForm form = new FilledForm();
                form.setName("Test request");
                form.setSource(new Account("testUser", "Test", "User", "Client", "Password"));
                ArrayList<String> arr = new ArrayList<>();
                arr.add("This is a test example.");
                arr.add("This is only here because clients can't yet submit service requests.");
                arr.add("Sample text.");
                form.setTextSequence(arr);
                ArrayList<FilledForm> arr2 = new ArrayList<>();
                arr2.add(form);
                user.setRequests(arr2);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestsButton.setEnabled(user.getRequests().size() != 0);
        String str = reqButtonText + " (" + user.getRequests().size() + ")";
        requestsButton.setText(str);
    }
}