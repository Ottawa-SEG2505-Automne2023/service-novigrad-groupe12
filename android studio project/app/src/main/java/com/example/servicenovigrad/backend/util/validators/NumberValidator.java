package com.example.servicenovigrad.backend.util.validators;

import android.widget.TextView;

import com.example.servicenovigrad.backend.util.Updatable;

public class NumberValidator extends FieldValidator {
    public NumberValidator(Updatable origin, TextView fieldLabel, String type) {
        super(origin, fieldLabel, type);
    }

    protected boolean validateText(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {return false;}
        }
        return super.validateText(s);
    }
}
