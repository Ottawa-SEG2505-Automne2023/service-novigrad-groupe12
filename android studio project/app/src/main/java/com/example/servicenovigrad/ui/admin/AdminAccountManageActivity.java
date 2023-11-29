package com.example.servicenovigrad.ui.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.AdminUserAdapter;
import com.example.servicenovigrad.backend.account.Account;
import com.example.servicenovigrad.backend.util.DataModifiedHook;

public class AdminAccountManageActivity extends AppCompatActivity {
    private ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account_manage);

        // Get the ListView
        usersListView = findViewById(R.id.usersListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the ListView's adapter
        usersListView.setAdapter(new AdminUserAdapter(this, DatabaseHandler.getUserList()));

        // Whenever the users list gets updated, update the adapter as well
        DatabaseHandler.addOnModifiedEvent("users",
                new DataModifiedHook(v ->
                        usersListView.setAdapter(new AdminUserAdapter(AdminAccountManageActivity.this, DatabaseHandler.getUserList())),
                        "AdminMainActivity-UpdateUsersList")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cleanup the event listener
        DatabaseHandler.removeOnModifiedEvent("users", "AdminMainActivity-UpdateUsersList");
    }

    // Very similar to what was shown in lab 5
    public void showAccountDeletionConfirmation(Account account) {
        // Creating the layout inflater
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_account_deletion, null);
        dialogBuilder.setView(dialogView);

        // Setting layout-specific values
        TextView accountDisplay = dialogView.findViewById(R.id.accountDisplayText);
        String accountInfo = account.getRole() + ": " + account.getUsername();
        accountDisplay.setText(accountInfo);

        Button yes = dialogView.findViewById(R.id.confirmDelete);
        Button no  = dialogView.findViewById(R.id.declineDelete);

        // Show the warning dialog
        dialogBuilder.setTitle("Confirmation");
        AlertDialog b = dialogBuilder.create();
        b.show();

        // Button event listeners
        yes.setOnClickListener(v -> {
            DatabaseHandler.deleteUser(account);
            b.dismiss();
        });

        no.setOnClickListener(v -> b.dismiss());
    }
}