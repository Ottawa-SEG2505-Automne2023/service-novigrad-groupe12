package com.example.servicenovigrad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.example.servicenovigrad.backend.ServiceForm;
import com.example.servicenovigrad.backend.ServicesHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

        /*
        ServiceForm s = new ServiceForm();
        s.setName("A");
        s.addTextField("A");
        s.addNumberField("B");
        List<String> spinnerElements = new ArrayList<>();
        spinnerElements.add("elem 1");
        spinnerElements.add("elem 2");
        spinnerElements.add("elem 3");
        s.addSpinner("C", spinnerElements);
        ServicesHandler.addService(s);
         */

        /*
        FirebaseDatabase.getInstance().getReference("globalServices/A").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FORM RETRIEVAL", snapshot.getValue(ServiceForm.class).getElements().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */
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