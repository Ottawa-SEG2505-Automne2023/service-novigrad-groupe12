package com.example.servicenovigrad.backend.util.validators;

import android.widget.TextView;

import com.example.servicenovigrad.backend.util.validators.FieldValidator;
import com.example.servicenovigrad.backend.util.Updatable;

// TextWatcher that validates usernames & passwords as they're entered into their respective fields
public class UserPassValidator extends FieldValidator {
    // A String of allowed special characters
    protected String validSpecials = ".!@#$%^&*-+'_";

    public UserPassValidator(Updatable origin, TextView fieldLabel, String type) {
        super(origin, fieldLabel, type);
    }

    // Verifies that a username is valid (contains only alphanumeric characters & allowed specials)
    protected boolean validateText(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i)) && getValidSpecials().indexOf(s.charAt(i)) == -1) {return false;}
        }
        return super.validateText(s);
    }

    public String getValidSpecials() {return validSpecials;}
}