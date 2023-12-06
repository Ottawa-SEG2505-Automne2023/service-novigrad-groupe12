package com.example.servicenovigrad.ui.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.CompleteBranch;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseServiceActivity extends AppCompatActivity {
    // Adapter for the services
    private static class ServiceAdapter extends ArrayAdapter<ServiceForm> {
        private final AppCompatActivity context;
        private final List<ServiceForm> services;
        private final String target;

        public ServiceAdapter(@NonNull AppCompatActivity context, List<ServiceForm> services, String target) {
            super(context, R.layout.admin_service_list_layout, services);
            this.context = context;
            this.services = services;
            this.target = target;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Reuse an old layout
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.admin_service_list_layout, null, true);

            // Setup functionality
            TextView formName = listViewItem.findViewById(R.id.formName);
            formName.setText(services.get(position).getName());

            Button editFormButton = listViewItem.findViewById(R.id.editFormButton);
            editFormButton.setText(R.string.create_demand);
            editFormButton.setOnClickListener(v -> services.get(position).open(context, "fill", target));

            return listViewItem;
        }
    }

    private CompleteBranch targetBranch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_service);

        // Finding the targeted branch
        for (CompleteBranch branch : DatabaseHandler.getBranches()) {
            if (branch.getUsername().equals(getIntent().getStringExtra("target"))) {
                targetBranch = branch;
                // Getting the offered services
                List<ServiceForm> services = new ArrayList<>();
                for (ServiceForm service : DatabaseHandler.getServicesList()) {
                    if (targetBranch.getServiceMap().containsKey(service.getName())) {
                        if (Boolean.TRUE.equals(targetBranch.getServiceMap().get(service.getName()))) {
                            services.add(service);
                        }
                    }
                }

                // Setting up the adapter
                ListView serviceList = findViewById(R.id.serviceBrowser);
                serviceList.setAdapter(new ServiceAdapter(this, services, targetBranch.getUsername()));
                break;
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        // Ask the user to rate the branch once they've finished their request
        rateBranch();
    }

    private void rateBranch() {
        // Creating the layout inflater
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_bubble, null);
        dialogBuilder.setView(dialogView);

        // Setting layout-specific values
        TextView branchName = dialogView.findViewById(R.id.branchNameText);
        String str = "Évaluez la succursale: " + targetBranch.getAddress();
        branchName.setText(str);

        Spinner ratingSpinner = dialogView.findViewById(R.id.ratingSpinner);
        ratingSpinner.setSelection(4);
        Button submitBtn = dialogView.findViewById(R.id.submitRatingButton);

        // Show the dialog
        dialogBuilder.setTitle("Évaluez votre expérience ");
        AlertDialog b = dialogBuilder.create();
        b.show();

        // The button
        submitBtn.setOnClickListener(v -> DatabaseHandler.getDatabase().getReference("completeBranches/" + targetBranch.getUsername() + "/ratingSpread/r" + ratingSpinner.getSelectedItem()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If the branch already has ratings of this type, increase the counter
                if (snapshot.exists()) {
                    snapshot.getRef().setValue(snapshot.getValue(Integer.class) + 1);
                // Otherwise, start at 1
                } else {
                    snapshot.getRef().setValue(1);
                }
                // Close the popup
                b.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RATING ERROR", "Couldn't connect to database: " + error.getMessage());
            }
        }));
    }
}