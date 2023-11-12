package com.example.servicenovigrad.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.services.ElementType;
import com.example.servicenovigrad.backend.services.FormElement;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.services.formdata.ServiceNameValidator;
import com.example.servicenovigrad.backend.util.Updatable;

public class EditFormActivity extends AppCompatActivity implements Updatable {
    private ListView list;
    private Spinner typeSpinner;
    private Button addButton;
    private Button saveButton;
    private TextView namePrompt;
    private EditText nameField;
    private ServiceForm service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_form);

        // Get things
        list = findViewById(R.id.elementList);
        typeSpinner = findViewById(R.id.typeSpinner);
        addButton = findViewById(R.id.addElementButton);
        saveButton = findViewById(R.id.saveServiceButton);
        namePrompt = findViewById(R.id.serviceNamePrompt);
        nameField = findViewById(R.id.serviceNameField);

        // If the opened form exists, load its data
        for (ServiceForm s : DatabaseHandler.getServicesList()) {
            if (s.getName().trim().equals(getIntent().getStringExtra("form"))) {
                service = s;
                break;
            }
        }

        // If not, start fresh
        if (service == null) {service = new ServiceForm();}

        // Fill in the name text and add its validator
        nameField.setText(service.getName());
        nameField.addTextChangedListener(new ServiceNameValidator(this, namePrompt, "Nom"));

        // Button that adds elements to the form
        addButton.setOnClickListener(v -> {
            // Get the type of the element to add
            ElementType type;
            switch (typeSpinner.getSelectedItem().toString()) {
                case "TextField":
                    type = ElementType.TEXTFIELD;
                case "NumberField":
                    type = ElementType.NUMBERFIELD;
                default:
                    type = ElementType.SPINNER;
            }
            // Create it and add it to the form
            FormElement element = new FormElement();
            element.setType(type);
            service.getElements().add(element);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void update() {
        saveButton.setEnabled(nameField.getTextColors().getDefaultColor() != 0xFFFF0000);
    }
}