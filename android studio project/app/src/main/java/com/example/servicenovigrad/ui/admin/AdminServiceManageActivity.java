package com.example.servicenovigrad.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.services.AdminServiceAdapter;
import com.example.servicenovigrad.backend.services.ServiceForm;

public class AdminServiceManageActivity extends AppCompatActivity {
    private ListView serviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_manage);

        // Setup
        serviceList = findViewById(R.id.usersListView);
        findViewById(R.id.adminAddServiceButton).setOnClickListener(v -> {
            // Create a new form and begin editing it.
            new ServiceForm().open(this, "edit");
        });
    }

    @Override
    protected void onStart() {
        // Start the adapter
        super.onStart();
        serviceList.setAdapter(new AdminServiceAdapter(this, DatabaseHandler.getServicesList()));
    }
}