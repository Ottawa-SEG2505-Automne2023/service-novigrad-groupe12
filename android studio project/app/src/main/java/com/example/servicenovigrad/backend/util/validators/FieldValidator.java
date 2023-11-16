package com.example.servicenovigrad.backend.util.validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.example.servicenovigrad.backend.util.Updatable;

// TextWatcher that validates text fields as they're entered into their respective fields to ensure they have characters
public class FieldValidator implements TextWatcher {

    // Instance variables, references to other objects & status text
    protected final Updatable origin;
    protected final TextView fieldLabel;
    protected final String initialText;
    protected final String errorText;
    private boolean canBeEmpty = false;

    public FieldValidator(Updatable origin, TextView fieldLabel, String type) {
        this.origin = origin;
        this.fieldLabel = fieldLabel;
        initialText = fieldLabel.getText().toString();
        errorText = type + " invalide!";
    }

    // When the EditText changes, check if it's valid. Update the UI accordingly.
    public void afterTextChanged(Editable s) {
        if (!validateText(s)) {
            fieldLabel.setTextColor(0xFFFF0000);
            fieldLabel.setText(errorText);
        } else {
            fieldLabel.setTextColor(0xFF000000);
            fieldLabel.setText(initialText);
        }
        origin.update();
    }

    // No implementation currently needed, has to be here for the implementation of the Interface
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    // No implementation currently needed, has to be here for the implementation of the Interface
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    // Enables an empty field
    public void allowEmpty() {canBeEmpty = true;}

    // Verifies that a username is valid (contains only alphanumeric characters & allowed specials)
    protected boolean validateText(CharSequence s) {
        return canBeEmpty || s.length() > 0;
    }
}