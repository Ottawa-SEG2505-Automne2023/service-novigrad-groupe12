package com.example.servicenovigrad.ui.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.backend.services.FilledForm;
import com.example.servicenovigrad.backend.services.ServiceForm;

import java.util.HashMap;
import java.util.List;

public class BranchServiceManage extends AppCompatActivity {
    // Adapter for the ListView
    private static class BranchRequestAdapter extends ArrayAdapter<FilledForm> {
        private final AppCompatActivity context;
        private final List<FilledForm> requests;
        public BranchRequestAdapter(@NonNull AppCompatActivity context, List<FilledForm> requests) {
            super(context, R.layout.admin_service_list_layout, requests);
            this.context = context;
            this.requests = requests;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.admin_service_list_layout, null, true);

            FilledForm req = requests.get(position);
            TextView formName = listViewItem.findViewById(R.id.formName);
            String str = req.getSource().getNom() + ", " + req.getSource().getPrenom() + ": " + req.getName();
            formName.setText(str);

            Button btn = listViewItem.findViewById(R.id.editFormButton);
            btn.setText(R.string.view_request);
            btn.setOnClickListener(v -> {
                Intent intent = new Intent(context, BranchApproveRequestActivity.class);
                intent.putExtra("requestNumber", position);
                context.startActivity(intent);
            });

            return listViewItem;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_service_manage);

        BranchAccount user = (BranchAccount) DatabaseHandler.user;

        ListView requestView = findViewById(R.id.usersListView);
        requestView.setAdapter(new BranchRequestAdapter(this, user.getRequests()));
    }
}