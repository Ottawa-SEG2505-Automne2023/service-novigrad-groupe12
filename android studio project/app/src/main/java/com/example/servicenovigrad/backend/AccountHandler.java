package com.example.servicenovigrad.backend;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.ui.AdminMainActivity;
import com.example.servicenovigrad.ui.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AccountHandler {
    public static Account user;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference usersRef = database.getReference("users");
    private static ArrayList<Account> users = new ArrayList<>();

    public static Class loginAsUser(Account account) {
        user = account;
        if (user.getRole().equals("administrateur")) {
            // Admin login

            // Populate the user queue & update it when it changes
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Clear old user list
                    users = new ArrayList<>();
                    for (DataSnapshot user : snapshot.getChildren()) {
                        PriorityQueue<Account> userQueue = new PriorityQueue<>();
                        // Only get accounts that are not the one we just logged into
                        if (!account.getUsername().equals(user.child("username").getValue(String.class))) {
                            userQueue.add(user.getValue(Account.class));
                        }
                        // Fill the list
                        while (!userQueue.isEmpty()) {
                            users.add(userQueue.remove());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("AccountHandler", "Cannot connect to database: " + error.getMessage());
                }
            });

            // Launch the admin activity
            return AdminMainActivity.class;
        }
        // Other login
        return MainActivity.class;
    }
}
