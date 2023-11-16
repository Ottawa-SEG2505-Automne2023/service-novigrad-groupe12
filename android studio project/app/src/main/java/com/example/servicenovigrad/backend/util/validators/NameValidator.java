package com.example.servicenovigrad.backend.util.validators;

import android.widget.TextView;

import com.example.servicenovigrad.backend.util.Updatable;

// Validates service names
public class NameValidator extends UserPassValidator {
    protected String validSpecials = super.validSpecials + ", ";
    public NameValidator(Updatable origin, TextView fieldLabel, String type) {
        super(origin, fieldLabel, type);
    }

    public String getValidSpecials() {return validSpecials;}
}
