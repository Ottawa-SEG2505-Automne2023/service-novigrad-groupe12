package com.example.servicenovigrad.backend;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// TextWatcher that validates usernames & passwords as they're entered into their respective fields
public class FieldValidator implements TextWatcher {
    // A String of allowed special characters
    private static final String validSpecials = ".!@#$%^&*-+'_";

    // Instance variables, references to other objects & status text
    private final Updatable origin;
    private final TextView fieldLabel;
    private final String initialText;
    private final String errorText;

    public FieldValidator(Updatable origin, TextView fieldLabel, String type) {
        this.origin = origin;
        this.fieldLabel = fieldLabel;
        initialText = fieldLabel.getText().toString();
        errorText = type + " invalide!";
    }

    // When the EditText changes, check if it's a valid username/password. Update the UI accordingly.
    public void afterTextChanged(Editable s) {
        if (!validateText(s)) {
            fieldLabel.setTextColor(0xFFFF0000);
            fieldLabel.setText(errorText);
        } else {
            fieldLabel.setTextColor(0xFF000000);
            fieldLabel.setText(initialText);
        }
        origin.updateUI();
    }

    // No implementation currently needed, has to be here for the implementation of the Interface
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    // No implementation currently needed, has to be here for the implementation of the Interface
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    // Verifies that a username is valid (contains only alphanumeric characters & allowed specials)
    private static boolean validateText(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            if (!(Character.isLetterOrDigit(s.charAt(i)) || validSpecials.indexOf(s.charAt(i)) != -1)) {return false;}
        }
        return true;
    }
}