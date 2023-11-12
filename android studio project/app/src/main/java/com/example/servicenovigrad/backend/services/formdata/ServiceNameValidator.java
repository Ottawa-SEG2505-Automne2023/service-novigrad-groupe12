package com.example.servicenovigrad.backend.services.formdata;

import android.widget.TextView;

import com.example.servicenovigrad.backend.account.UserPassValidator;
import com.example.servicenovigrad.backend.util.Updatable;

// Validates service names
public class ServiceNameValidator extends UserPassValidator {
    protected String validSpecials = super.validSpecials + " ";
    public ServiceNameValidator(Updatable origin, TextView fieldLabel, String type) {
        super(origin, fieldLabel, type);
    }

    public String getValidSpecials() {return validSpecials;}
}
