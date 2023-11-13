package com.example.servicenovigrad.backend.services;

// Stores information on an element of a ServiceForm
public class FormElement {
    private ElementType type;
    private String label;
    private ExtraFormData extra;
    public FormElement() {}
    public FormElement(ElementType type, String label, ExtraFormData extra) {
        setType(type);
        setLabel(label);
        setExtra(extra);
    }
    public void setType(ElementType type) {this.type = type;}
    public void setLabel(String label) {this.label = label;}
    public void setExtra(ExtraFormData extra) {this.extra = extra;}
    public ElementType getType() {return type;}
    public String getLabel() {return label;}
    public ExtraFormData getExtra() {return extra;}
}
