package com.example.servicenovigrad.ui.branch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.servicenovigrad.R;

public class BranchApproveRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_approve_request);

        // Request # stored in intExtra requestNumber

        ListView formViewer = findViewById(R.id.formViewer);

        Button yes = findViewById(R.id.yesBtn);
        Button no = findViewById(R.id.noBtn);
    }
}