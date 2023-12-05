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
}
