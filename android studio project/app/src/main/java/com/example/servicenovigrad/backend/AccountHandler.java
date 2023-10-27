package com.example.servicenovigrad.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountHandler {
    public static String user;
    private FirebaseDatabase database;

    public AccountHandler() {
        database = FirebaseDatabase.getInstance();
    }
}
