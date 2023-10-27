package com.example.servicenovigrad.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.AccountHandler;
import com.example.servicenovigrad.backend.FieldValidator;
import com.example.servicenovigrad.backend.Updatable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements Updatable {
    private Spinner roleSpinner;
    private EditText signupUsername, signupPrenom, signupNom, signupPassword;
    private TextView signupUsernamePrompt, signupPasswordPrompt;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        roleSpinner = (Spinner) findViewById(R.id.roleSpinner);

        signupUsername = (EditText) findViewById(R.id.signupUsername);
        signupUsernamePrompt = (TextView) findViewById(R.id.signupUsernamePrompt);
        signupUsername.addTextChangedListener(new FieldValidator(this, signupUsernamePrompt, "Nom d'utilisateur"));

        // No need to validate names, there could be all sorts of special characters for all we know!
        signupPrenom = (EditText) findViewById(R.id.signupPrenom);
        signupNom = (EditText) findViewById(R.id.signupNom);

        signupPassword = (EditText) findViewById(R.id.signupPassword);
        signupPasswordPrompt = (TextView) findViewById(R.id.signupPasswordPrompt);
        signupPassword.addTextChangedListener(new FieldValidator(this, signupPasswordPrompt, "Mot de passe"));

        btnSignup = (Button) findViewById(R.id.btnSignup);
    }

    // Conditionally enable the button
    public void update() {
        btnSignup.setEnabled(signupUsernamePrompt.getTextColors().getDefaultColor() != 0xFFFF0000 && signupPasswordPrompt.getTextColors().getDefaultColor() != 0xFFFF0000 && signupUsername.getText().length() != 0 && signupPassword.getText().length() >= 8);
    }

    public void attemptSignup(View view) {
        // Gather String resources
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String username = signupUsername.getText().toString();
        String root = "users/" + username + "/";
        String prenom = signupPrenom.getText().toString();
        String nom = signupNom.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();
        String password = signupPassword.getText().toString();

        // Set values in the database, creating the account
        DatabaseReference prenomRef = database.getReference(root + "prenom");
        prenomRef.setValue(prenom);

        DatabaseReference nomRef = database.getReference(root + "nom");
        nomRef.setValue(nom);

        DatabaseReference roleRef = database.getReference(root + "role");
        roleRef.setValue(role);

        DatabaseReference passwordRef = database.getReference(root + "password");
        passwordRef.setValue(password);

        // Login automatically
        AccountHandler.user = username;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}