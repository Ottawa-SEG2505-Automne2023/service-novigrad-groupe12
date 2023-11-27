package com.example.servicenovigrad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.backend.util.validators.UserPassValidator;
import com.example.servicenovigrad.backend.util.Updatable;
import com.example.servicenovigrad.backend.account.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity implements Updatable {
    private Spinner roleSpinner;
    private EditText signupUsername, signupPrenom, signupNom, signupPassword;
    private TextView signupUsernamePrompt, signupPasswordPrompt;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        roleSpinner = findViewById(R.id.roleSpinner);

        signupUsername = findViewById(R.id.signupUsername);
        signupUsernamePrompt = findViewById(R.id.signupUsernamePrompt);
        signupUsername.addTextChangedListener(new UserPassValidator(this, signupUsernamePrompt, "Nom d'utilisateur"));

        // No need to validate names, there could be all sorts of special characters for all we know!
        signupPrenom = findViewById(R.id.signupPrenom);
        signupNom = findViewById(R.id.signupNom);

        signupPassword = findViewById(R.id.signupPassword);
        signupPasswordPrompt = findViewById(R.id.signupPasswordPrompt);
        signupPassword.addTextChangedListener(new UserPassValidator(this, signupPasswordPrompt, "Mot de passe"));

        btnSignup = findViewById(R.id.btnSignup);
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

        DatabaseReference acctRef = database.getReference(root);
        acctRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    signupUsernamePrompt.setText(getString(R.string.user_already_exists));
                    signupUsernamePrompt.setTextColor(0xFFFF0000);
                    btnSignup.setEnabled(false);
                } else {
                    String prenom = signupPrenom.getText().toString();
                    String nom = signupNom.getText().toString();
                    String role = roleSpinner.getSelectedItem().toString();
                    String password = signupPassword.getText().toString();

                    Account acct;
                    if (role.equals("Employé de la succursale")) {
                        acct = new BranchAccount(username, nom, prenom, role, password);
                    } else {
                        acct = new Account(username, nom, prenom, role, password);
                    }
                    acctRef.setValue(acct);
                    DatabaseHandler.user = acct;
                    startActivity(new Intent(getApplicationContext(), DatabaseHandler.loginAsUser(acct)));
                }
                acctRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SignupActivity", "Cannot connect to database: " + error.getMessage());
                acctRef.removeEventListener(this);
            }
        });
    }
}