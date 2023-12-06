package com.example.servicenovigrad.ui.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.CompleteBranch;
import com.example.servicenovigrad.ui.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WelcomeActivity {
    // Adapter for displaying the branches to the client
    private static class BranchAdapter extends ArrayAdapter<CompleteBranch> {
        private final AppCompatActivity context;
        private final List<CompleteBranch> branches;
        public BranchAdapter(@NonNull AppCompatActivity context, List<CompleteBranch> branches) {
            super(context, R.layout.branch_display, branches);
            this.context = context;
            this.branches = branches;
        }
        @SuppressLint("DefaultLocale")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.branch_display, null, true);

            CompleteBranch branch = branches.get(position);

            TextView branchName = listViewItem.findViewById(R.id.branchName);
            branchName.setText(branch.getAddress());

            TextView branchRating = listViewItem.findViewById(R.id.branchRating);
            String str = "";
            double rating = branch.rating();
            // Treating special cases
            if (rating == -1) {str = "Aucune note pour l'instant";}
            else {str += String.format("%.1f", rating) + "/5.0";}
            branchRating.setText(str);

            Button serviceButton = listViewItem.findViewById(R.id.serviceButton);
            serviceButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, BrowseServiceActivity.class);
                intent.putExtra("target", branch.getUsername());
                context.startActivity(intent);
            });

            return listViewItem;
        }

        public void changeData(List<CompleteBranch> data) {
            branches.clear();
            if (data != null) {branches.addAll(data);}
            notifyDataSetChanged();
        }
    }
    private EditText searchBox;
    private BranchAdapter adapter;
    private String searchMode = "address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gathering things
        welcomeText = findViewById(R.id.mainWelcomeText);
        searchBox = findViewById(R.id.searchBox);
        RadioButton searchAddress = findViewById(R.id.searchAddress);
        RadioButton searchHours = findViewById(R.id.searchHours);
        RadioButton searchService = findViewById(R.id.searchService);
        Button searchButton = findViewById(R.id.searchButton);

        // Search by address by default
        searchAddress.toggle();

        // Set up search modes
        searchAddress.setOnClickListener(v -> {searchBox.setText(""); searchMode = "address"; searchBox.setHint("Entrez une addresse");});
        searchHours.setOnClickListener(v -> {searchBox.setText(""); searchMode = "hours"; searchBox.setHint("Entrez une heure entre \"9:00\" et \"5:00\"");});
        searchService.setOnClickListener(v -> {searchBox.setText(""); searchMode = "service"; searchBox.setHint("Rechercher une service");});
        searchButton.setOnClickListener(v -> searchBranch());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set up the ListView
        adapter = new BranchAdapter(this, new ArrayList<>(DatabaseHandler.getBranches()));
        ListView branchesBox = findViewById(R.id.branchesBox);
        branchesBox.setAdapter(adapter);

        // Show notifications
        if (DatabaseHandler.user.getNotifications().size() > 0) {
            showNotification();
        }
    }

    private void searchBranch() {
        // Do search
        List<CompleteBranch> filteredBranches = new ArrayList<>();
        String[] words = searchBox.getText().toString().split("[, ]");
        if (words.length == 0) {adapter.changeData(null); return;}

        // For each branch
        for (CompleteBranch branch : DatabaseHandler.getBranches()) {
            // For each word in the search box
            for (String word : words) {
                // If we're in address mode,
                if (searchMode.equals("address")) {
                    // And there's a similarity, add the branch to the list
                    if (branch.getAddress().contains(word)) {
                        filteredBranches.add(branch);
                        break;
                    }
                // If we're in hours mode,
                } else if (searchMode.equals("hours")) {
                    // And the branch is open at that time, add the branch to the list
                    if (branch.isOpenAt(CompleteBranch.realHoursToStoredHours(searchBox.getText().toString()))) {
                        filteredBranches.add(branch);
                    }
                // If we're in service mode,
                } else {
                    // And there's a similarity in the services offered, add the branch to the list
                    for (String key : branch.getServiceMap().keySet()) {
                        if (key.toLowerCase().contains(word.toLowerCase())) {
                            if (Boolean.TRUE.equals(branch.getServiceMap().get(key))) {
                                filteredBranches.add(branch);
                            }
                        }
                    }
                }
            }
        }
        // Make the adapter display the filtered set of branches
        adapter.changeData(filteredBranches);
    }

    private void showNotification() {
        // Creating the layout inflater
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.notification_bubble, null);
        dialogBuilder.setView(dialogView);

        // Show the notification at the end of the list
        TextView notificationText = dialogView.findViewById(R.id.notificationText);
        List<String> notifications = DatabaseHandler.user.getNotifications();
        notificationText.setText(notifications.get(notifications.size() - 1));
        Button dismissButton = dialogView.findViewById(R.id.dismissButton);

        // Show the dialog
        dialogBuilder.setTitle("Notification");
        AlertDialog b = dialogBuilder.create();
        b.show();

        dismissButton.setOnClickListener(v -> {
            // Close the popup and remove the notification
            b.dismiss();
            notifications.remove(notifications.size() - 1);
            DatabaseHandler.getDatabase().getReference("users/" + DatabaseHandler.user.getUsername() + "/notifications/" + notifications.size()).removeValue();
            // Recursively show any remaining notifications
            if (DatabaseHandler.user.getNotifications().size() > 0) {showNotification();}
        });
    }
}
