package com.example.servicenovigrad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.FieldValidator;
import com.example.servicenovigrad.backend.Updatable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements Updatable {
    private EditText userField, passField;
    private TextView userLabel, passLabel;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userField = (EditText) findViewById(R.id.loginUsernameInput);
        userLabel = (TextView) findViewById(R.id.loginUsernamePrompt);
        userField.addTextChangedListener(new FieldValidator(this, userLabel, "Nom d'utilisateur"));

        passField = (EditText) findViewById(R.id.loginPasswordInput);
        passLabel = (TextView) findViewById(R.id.loginPasswordPrompt);
        passField.addTextChangedListener(new FieldValidator(this, passLabel, "Mot de passe"));

        btnLogin = (Button) findViewById(R.id.loginButton);
    }

    // Enables/disables the login button depending on the states of the input fields
    public void updateUI() {
        btnLogin.setEnabled(userLabel.getTextColors().getDefaultColor() != 0xFFFF0000 && passLabel.getTextColors().getDefaultColor() != 0xFFFF0000 && userField.getText().length() != 0 && passField.getText().length() >= 8);
    }

    // Attempts to log in
    public void attemptLogin(View view) {
        // Get password data from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users/" + userField.getText() + "/password");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If this path exists
                if (snapshot.exists()) {
                    // And the passwords match
                    if (passField.getText().toString().equals(snapshot.getValue(String.class))) {
                        // Enter the main activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    // If they don't,
                    else {
                        passLabel.setTextColor(0xFFFF0000);
                        passLabel.setText(getString(R.string.wrong_password));
                        btnLogin.setEnabled(false);
                    }
                // If it doesn't, the user does not exist
                } else {
                    userLabel.setTextColor(0xFFFF0000);
                    userLabel.setText(getString(R.string.wrong_user));
                    btnLogin.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase", "Cannot read data: " + error.getCode());
            }
        });
    }

    // Goes to the account creation menu
    public void signupMenu(View view) {
        
    }
}