package com.example.servicenovigrad.backend.services;

// Stores information on an element of a ServiceForm
public class FormElement implements Comparable<FormElement> {
    private ElementType type;
    private String label;
    private ExtraFormData extra;
    public FormElement() {}
        /**
     * Constructs a FormElement with specified type, label, and extra data.
     *
     * @param type  The type of the form element.
     * @param label The label of the form element.
     * @param extra Additional data for the form element.
     */
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
    // Added purely for compatibility as a key of a TreeMap
    public int compareTo(FormElement b) {return toString().compareTo(b.toString());}
}
