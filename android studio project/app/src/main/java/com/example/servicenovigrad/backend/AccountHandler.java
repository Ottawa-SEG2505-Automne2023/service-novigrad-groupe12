package com.example.servicenovigrad.backend;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.ui.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountHandler {
    public static Account user;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static Class loginAsUser(Account account) {
        user = account;
        if (user.getRole().equals("administrateur")) {
            // Admin login
        }
        // Other login
        return MainActivity.class;
    }
}
