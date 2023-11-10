package com.example.servicenovigrad.backend;

import java.util.ArrayList;
import java.util.List;

public class ServiceForm {
    private List<FormElement> elements = new ArrayList<>();
    private String name;
    public ServiceForm() {}
    public void setElements(List<FormElement> elements) {
        if (elements != null) {
            this.elements.clear();
            this.elements.addAll(elements);
        }
    }
    public void setName(String name) {this.name = name;}
    public List<FormElement> getElements() {return elements;}
    public String getName() {return name;}

    // ADDING ELEMENTS TO THE FORM
    public void addTextField(String label) {
        addElement(ElementType.TEXTFIELD, label, null);
    }
    public void addNumberField(String label) {
        addElement(ElementType.NUMBERFIELD, label, null);
    }
    public void addSpinner(String label, List<String> extra) {
        addElement(ElementType.SPINNER, label, extra);
    }
    private void addElement(ElementType type, String label, List<String> extra) {
        elements.add(new FormElement(type, label, extra));
    }
}
