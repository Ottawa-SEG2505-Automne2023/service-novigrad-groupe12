package com.example.servicenovigrad.backend.util.validators;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.servicenovigrad.backend.util.Updatable;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OldDateValidator extends FieldValidator {
    private int minAge;
    // Currently untested, will test when implementing employee and/or client stuff
    public OldDateValidator(Updatable origin, TextView fieldLabel, String type, int minAge) {
        super(origin, fieldLabel, type);
        this.minAge = minAge;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validateText(CharSequence s) {
        LocalDate lim = LocalDate.now().minusYears(minAge);
        LocalDate inDate = getDate(s);
        if (inDate == null) {return false;}

        return inDate.isBefore(lim);
    }

    // Gets the date of the form yyyy-mm-dd
    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate getDate(CharSequence s) {
        try {
            return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        catch (DateTimeException e) {
            return null;
        }
    }
}
