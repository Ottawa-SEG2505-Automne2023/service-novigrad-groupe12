package com.example.servicenovigrad.backend.services;

/*
import com.example.servicenovigrad.backend.util.validators.DateValidator;
import com.example.servicenovigrad.backend.util.validators.FieldValidator;
import com.example.servicenovigrad.backend.util.validators.NameValidator;
import com.example.servicenovigrad.backend.util.validators.NumberValidator;
import com.example.servicenovigrad.backend.util.validators.UserPassValidator;
*/

import java.util.ArrayList;
import java.util.List;

// Stores extra data for fields in ServiceForms
public class ExtraFormData {
    private int charLimit;
    private int validatorClass;
    private final List<String> elements = new ArrayList<>();
    public ExtraFormData() {}
    public int getCharLimit() {return charLimit;}
    public int getValidatorClass() {return validatorClass;}
    public List<String> getElements() {return elements;}
    public void setCharLimit(int charLimit) {this.charLimit = charLimit;}
    public void setValidatorClass(int validatorClass) {this.validatorClass = validatorClass;}
    public void setElements(List<String> elements) {this.elements.clear(); if (elements != null) {this.elements.addAll(elements);}}


    // PROOF OF CONCEPT, MAY OR MAY NOT BE USED LATER
    /*
    /**
     * Maps a position to a corresponding FieldValidator class.
     * 
     * @param pos The position representing a specific FieldValidator.
     * @return The class of the FieldValidator.
     */
    /*
    private static Class<? extends FieldValidator> positionToClass(int pos) {
        switch (pos) {
            case 0:
                return FieldValidator.class;
            case 1:
                return UserPassValidator.class;
            case 2:
                return NameValidator.class;
            case 3:
                return DateValidator.class;
            default:
                return NumberValidator.class;
        }
    }
    */
}
