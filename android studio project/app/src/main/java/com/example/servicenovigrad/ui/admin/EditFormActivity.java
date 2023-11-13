package com.example.servicenovigrad.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.services.ElementType;
import com.example.servicenovigrad.backend.services.FormElement;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.services.ExtraFormData;
import com.example.servicenovigrad.backend.services.FormElementAdapter;
import com.example.servicenovigrad.backend.util.Callable;
import com.example.servicenovigrad.backend.util.validators.NameValidator;
import com.example.servicenovigrad.backend.util.Updatable;

import java.util.List;

public class EditFormActivity extends AppCompatActivity implements Updatable {
    private RecyclerView list;
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
        nameField.addTextChangedListener(new NameValidator(this, namePrompt, "Nom"));

        // Button that adds elements to the form
        addButton.setOnClickListener(v -> {
            // Compile everything up until now
            compileForm();

            // Get the type of the element to add
            ElementType type;
            if (typeSpinner.getSelectedItem().toString().equals("TextField")) {
                type = ElementType.TEXTFIELD;
            } else {
                type = ElementType.SPINNER;
            }

            // Create it and add it to the form
            FormElement element = new FormElement();
            element.setType(type);
            service.getElements().add(element);

            // list.setAdapter(new FormElementAdapter(EditFormActivity.this, service.getElements()));
            refreshDisplay();
        });

        saveButton.setOnClickListener(v -> {compileForm(); DatabaseHandler.addService(service); finish();});

        list.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration spacer = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        spacer.getDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_OVER);
        list.addItemDecoration(spacer);

        /*
        // TEST
        service.setName("TEST SERVICE");
        FormElement a = new FormElement();
        a.setType(ElementType.TEXTFIELD);
        a.setLabel("NAME");
        FieldData b = new FieldData();
        b.setValidatorClass(2);
        b.setCharLimit(69);
        a.setExtra(b);
        service.getElements().add(a);
        Log.d("PUSHTEST", "PUSHING");
        DatabaseHandler.addService(service);
        Log.d("PUSHTEST", "POST-PUSH");

         */
    }

    @Override
    public void onStart() {
        super.onStart();
        list.setAdapter(new FormElementAdapter(this, service.getElements()));
    }

    @Override
    public void update() {
        saveButton.setEnabled(nameField.getTextColors().getDefaultColor() != 0xFFFF0000);
    }

    public void compileForm() {
        FormElementAdapter adapter = (FormElementAdapter) list.getAdapter();
        // Prevent a NullPointerException
        if (adapter.getItemCount() == 0) {return;}

        // Save each element
        List<FormElement> elements = service.getElements();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            int type = adapter.getItemViewType(i);
            // TextField
            if (type == 1) {
                FormElementAdapter.FieldViewHolder holder = (FormElementAdapter.FieldViewHolder) list.findViewHolderForAdapterPosition(i);
                ExtraFormData extra = new ExtraFormData();
                extra.setCharLimit(holder.getLimit());
                extra.setValidatorClass(holder.getValidator());

                elements.get(i).setLabel(holder.getName());
                elements.get(i).setExtra(extra);
            }
        }
        service.setName(nameField.getText().toString());
    }

    public void refreshDisplay() {
        list.invalidate();
    }
}