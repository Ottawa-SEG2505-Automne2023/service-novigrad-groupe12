package com.example.servicenovigrad.backend.util.validators;

import android.widget.TextView;

import com.example.servicenovigrad.backend.util.Updatable;

import java.util.regex.Pattern;

public class AddressValidator extends FieldValidator {
    // Pattern matches: "[n-digit number] [set of words accepting numbers, spaces & ['-.] (street name)], [set of words accepting numbers, spaces & ['-.] (city name)]"
    // Found the pattern allowing for accented letters here: https://stackoverflow.com/a/26900132
    private static final Pattern addressPattern = Pattern.compile("^\\d+\\s+[A-Za-zÀ-ÖØ-öø-ÿ\\s\\d'-.]+,\\s+[A-Za-zÀ-ÖØ-öø-ÿ\\s\\d'-.]+$");
    public AddressValidator(Updatable origin, TextView fieldLabel, String type) {
        super(origin, fieldLabel, type);
    }

    public boolean validateText(CharSequence s) {
        return addressPattern.matcher(s).matches();
    }
}
