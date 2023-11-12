package com.example.servicenovigrad.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.example.servicenovigrad.backend.DatabaseHandler;

// Base class for any landing activity that welcomes the user.
public class WelcomeActivity extends AppCompatActivity {
    protected TextView welcomeText;

    @Override
    protected void onStart() {
        super.onStart();

        String str = "Bienvenue, " + DatabaseHandler.user.getPrenom() + "! Vous êtes connecté en tant que " + DatabaseHandler.user.getRole() + ".";
        welcomeText.setText(str);
    }
}