package com.example.servicenovigrad.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.Account;
import com.example.servicenovigrad.backend.AccountHandler;
import com.example.servicenovigrad.backend.AdminUserAdapter;
import com.example.servicenovigrad.backend.DataModifiedHook;

public class AdminMainActivity extends AppCompatActivity {
    ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Get the ListView
        usersListView = findViewById(R.id.usersListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the ListView's adapter
        usersListView.setAdapter(new AdminUserAdapter(this, AccountHandler.getUserList()));

        // Whenever the users list gets updated, update the adapter as well
        AccountHandler.addOnModifiedEvent("users",
                new DataModifiedHook(v ->
                        usersListView.setAdapter(new AdminUserAdapter(AdminMainActivity.this,AccountHandler.getUserList())),
                        "AdminMainActivity-UpdateUsersList")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cleanup the event listener
        AccountHandler.removeOnModifiedEvent("users", "AdminMainActivity-UpdateUsersList");
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
            AccountHandler.deleteUser(account);
            b.dismiss();
        });

        no.setOnClickListener(v -> b.dismiss());
    }
}