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

    public boolean equals(ServiceForm other) {
        return id.equals(other.id);
    }


    public static List<ServiceForm> createSampleForms() {
        List<ServiceForm> sampleForms = new ArrayList<>();

        // Create and add some sample forms with different IDs and elements
        ServiceForm form1 = new ServiceForm();
        form1.setId("form1");
        form1.setName("Sample Form 1");
        form1.setElements(Arrays.asList(
            new FormElement(ElementType.TEXTFIELD, "Name", null),
            new FormElement(ElementType.SPINNER, "Service Type", new ExtraFormData())
        ));
        sampleForms.add(form1);

        ServiceForm form2 = new ServiceForm();
        form2.setId("form2");
        form2.setName("Sample Form 2");
        form2.setElements(Arrays.asList(
            new FormElement(ElementType.DOCUMENT, "Upload Document", null)
        ));
        sampleForms.add(form2);


        return sampleForms;
    }

    
}
