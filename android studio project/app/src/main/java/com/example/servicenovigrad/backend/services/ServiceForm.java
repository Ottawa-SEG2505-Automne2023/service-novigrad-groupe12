package com.example.servicenovigrad.backend.services;

import android.app.Activity;
import android.content.Intent;

import com.example.servicenovigrad.ui.FillFormActivity;
import com.example.servicenovigrad.ui.admin.EditFormActivity;

import java.util.ArrayList;
import java.util.List;

// Stores data for a service offered by ServiceNovigrad
public class ServiceForm {
    private final List<FormElement> elements = new ArrayList<>();
    private String name = "Nouveau service";
    private String id;
    public ServiceForm() {}
    public void setElements(List<FormElement> elements) {
        if (elements != null) {
            this.elements.clear();
            this.elements.addAll(elements);
        }
    }
    public void setName(String name) {this.name = name;}
    public void setId(String id) {this.id = id;}
    public List<FormElement> getElements() {return elements;}
    public String getName() {return name;}
    public String getId() {return id;}

    // ADDING ELEMENTS TO THE FORM
    public void addTextField(String label) {
        addElement(ElementType.TEXTFIELD, label, null);
    }
    public void addSpinner(String label, ArrayList<String> elements) {
        addElement(ElementType.SPINNER, label, new ExtraFormData(elements));
    }
    private void addElement(ElementType type, String label, ExtraFormData extra) {
        elements.add(new FormElement(type, label, extra));
    }

    // OPENING THE FORM
    public void open(Activity context, String mode) {
        Intent intent;
        // Edit mode (for admins)
        if (mode.equals("edit")) {
            intent = new Intent(context, EditFormActivity.class);
        // Fill mode (for clients)
        } else {
            intent = new Intent(context, FillFormActivity.class);
        }
        // Attach an identifier for this object
        intent.putExtra("formID", id);
        context.startActivity(intent);
    }
}
