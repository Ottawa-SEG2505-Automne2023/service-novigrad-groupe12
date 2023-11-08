package com.example.servicenovigrad.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.AccountHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeText = findViewById(R.id.welcomeText);
        String str = "Bienvenue, " + AccountHandler.user.getPrenom() + "! Vous êtes connecté en tant que " + AccountHandler.user.getRole() + ".";
        welcomeText.setText(str);
    }
}