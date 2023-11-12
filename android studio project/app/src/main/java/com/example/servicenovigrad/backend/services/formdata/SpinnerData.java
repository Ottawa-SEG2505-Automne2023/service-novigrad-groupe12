package com.example.servicenovigrad.backend.services.formdata;

import java.util.ArrayList;
import java.util.List;

// Stores the elements of Spinners in ServiceForms
public class SpinnerData implements ExtraFormData {
    private final List<String> elements;
    public SpinnerData() {elements = new ArrayList<>();}
    public SpinnerData(List<String> elements) {this(); this.elements.addAll(elements);}

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements.clear();
        if (elements != null) {this.elements.addAll(elements);}
    }
}
