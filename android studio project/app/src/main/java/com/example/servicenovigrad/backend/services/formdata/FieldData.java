package com.example.servicenovigrad.backend.services.formdata;

// Stores extra data for fields in ServiceForms
public class FieldData implements ExtraFormData {
    private int charLimit;
    private boolean validate;
    public FieldData() {}
    public int getCharLimit() {return charLimit;}
    public boolean getValidate() {return validate;}
    public void setCharLimit(int charLimit) {this.charLimit = charLimit;}
    public void setValidate(boolean validate) {this.validate = validate;}
}
