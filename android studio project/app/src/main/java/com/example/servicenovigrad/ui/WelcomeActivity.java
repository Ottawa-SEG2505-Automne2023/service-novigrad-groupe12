package com.example.servicenovigrad.ui;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.servicenovigrad.backend.DatabaseHandler;

/**
 * Base class for any landing activity that welcomes the user.
 */
public class WelcomeActivity extends AppCompatActivity {
    protected TextView welcomeText;

    @Override
    protected void onStart() {
        super.onStart();

        // Constructing the welcome message
        String welcomeMessage = String.format("Bienvenue, %s! Vous êtes connecté en tant que %s.",
                DatabaseHandler.user.getPrenom(), DatabaseHandler.user.getRole());

        welcomeText.setText(welcomeMessage);
    }
}
