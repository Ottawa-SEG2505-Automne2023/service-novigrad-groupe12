package com.example.servicenovigrad.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.backend.account.Account;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.backend.account.CompleteBranch;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.util.DataModifiedHook;
import com.example.servicenovigrad.ui.branch.EmployeeMainActivity;
import com.example.servicenovigrad.ui.admin.AdminMainActivity;
import com.example.servicenovigrad.ui.client.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class DatabaseHandler {
    public static Account user;
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // USER ACCOUNT STUFF
    private static final DatabaseReference usersRef = database.getReference("users");
    private static final ArrayList<Account> users = new ArrayList<>();
    private static final TreeMap<String, DataModifiedHook> onUsersModifiedEvents = new TreeMap<>();
    // SERVICES STUFF
    private static final DatabaseReference servicesRef = database.getReference("globalServices");
    private static final ArrayList<ServiceForm> services = new ArrayList<>();
    private static final ArrayList<CompleteBranch> branches = new ArrayList<>();

    // Logs in as a user (returns which activity to launch)
    public static Class<?> loginAsUser(Account account) {
        user = account;

        // Admin login
        if (user.getRole().equals("administrateur")) {
            // Populate the user queue & update it when it changes
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Clear old user list
                    users.clear();
                    PriorityQueue<Account> userQueue = new PriorityQueue<>();

                    // Repopulate
                    for (DataSnapshot user : snapshot.getChildren()) {
                        // Only get accounts that are not the one we just logged into
                        if (!account.getUsername().equals(user.child("username").getValue(String.class))) {
                            Account acct;
                            if (Objects.equals(user.child("role").getValue(String.class), "Employé de la succursale")) {
                                acct = user.getValue(BranchAccount.class);
                            } else {
                                acct = user.getValue(Account.class);
                            }
                            if (acct != null) {userQueue.add(acct);}
                        }
                    }
                    // Fill the list
                    while (!userQueue.isEmpty()) {
                        users.add(userQueue.remove());
                    }

                    // Call onModifiedEvents
                    for (DataModifiedHook hook : onUsersModifiedEvents.values()) {
                        hook.call();
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
        // Employee login
        else if (user.getRole().equals("Employé de la succursale")) {
            return EmployeeMainActivity.class;
        }

        // Client login
        return MainActivity.class;
    }

    // Add a hook to the targeted map
    public static void addOnModifiedEvent(String target, DataModifiedHook hook) {
        if (target.equals("users")) {
            onUsersModifiedEvents.put(hook.getKey(), hook);
        }
    }

    // Removes a hook from the targeted map
    public static void removeOnModifiedEvent(String target, String key) {
        if (target.equals("users")) {
            onUsersModifiedEvents.remove(key);
        }
    }

    // USER ACCOUNT METHODS
    // Returns the users list
    public static ArrayList<Account> getUserList() {
        return users;
    }

    // Deletes a specific user from the database
    public static void deleteUser(Account account) {
        usersRef.child(account.getUsername()).removeValue();
        if (account instanceof BranchAccount) {
            database.getReference("completeBranches/" + account.getUsername()).removeValue();
        }
    }

    public static List<CompleteBranch> getBranches() {return branches;}

    // SERVICES METHODS
    public static void addService(ServiceForm s) {
        DatabaseReference root;
        if (s.getId() == null) {root = servicesRef.push();}
        else {root = servicesRef.child(s.getId());}
        s.setId(root.getKey());
        root.setValue(s);
    }

    public static void deleteService(ServiceForm s) {
        if (s.getId() == null) {return;}
        servicesRef.child(s.getId()).removeValue();
    }

    public static ArrayList<ServiceForm> getServicesList() {return services;}

    public static FirebaseDatabase getDatabase() {return database;}

    // Sends a notification to the given user
    public static void notify(String username, String message) {
        DatabaseReference ref = usersRef.child(username);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If the account already has notifications
                if (snapshot.exists()) {
                    if (snapshot.child("notifications").exists()) {
                        // Append to the end of the list
                        long ext = snapshot.getChildrenCount();
                        ref.child("/notifications/" + ext).setValue(message);
                    }
                    // Otherwise
                    else {
                        // Create the first instance in the list
                        ref.child("/notifications/0").setValue(message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("AccountHandler", "Cannot connect to database: " + error.getMessage());
            }
        });
    }

    public static void runServiceLoader() {
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                services.clear();
                for (DataSnapshot service : snapshot.getChildren()) {
                    services.add(service.getValue(ServiceForm.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("AccountHandler", "Cannot connect to database: " + error.getMessage());
            }
        });
    }
}
