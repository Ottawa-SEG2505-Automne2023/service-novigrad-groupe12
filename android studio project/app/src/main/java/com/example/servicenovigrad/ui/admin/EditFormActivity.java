package com.example.servicenovigrad.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
import com.example.servicenovigrad.backend.services.EditFormElementAdapter;
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

        // If the opened form exists in the database, load its data
        for (ServiceForm s : DatabaseHandler.getServicesList()) {
            if (s.getId().equals(getIntent().getStringExtra("formID"))) {
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
            // Compile everything up until now to the ServiceForm
            compileForm();

            // Get the type of the element to add
            ElementType type;
            switch (typeSpinner.getSelectedItem().toString()) {
                case "TextField":
                    type = ElementType.TEXTFIELD;
                    break;
                case "Spinner":
                    type = ElementType.SPINNER;
                    break;
                default:
                    type = ElementType.DOCUMENT;
                    break;
            }

            // Create it and add it to the form
            FormElement element = new FormElement();
            element.setType(type);
            service.getElements().add(element);

            // Refresh the display
            list.getAdapter().notifyItemInserted(service.getElements().size());
        });

        // Saving the form to the database
        saveButton.setOnClickListener(v -> {compileForm(); DatabaseHandler.addService(service); finish();});

        // Viasual stuff for the RecyclerView
        list.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration spacer = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        spacer.getDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_OVER);
        list.addItemDecoration(spacer);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set up the display
        list.setAdapter(new EditFormElementAdapter(EditFormActivity.this, service.getElements()));
    }

    @Override
    public void update() {
        // Update the save button
        saveButton.setEnabled(nameField.getTextColors().getDefaultColor() != 0xFFFF0000);
    }

    public void compileForm() {
        EditFormElementAdapter adapter = (EditFormElementAdapter) list.getAdapter();
        // Prevent a NullPointerException
        if (adapter.getItemCount() == 0) {return;}

        // Save each element
        List<FormElement> elements = service.getElements();
        List<EditFormElementAdapter.BaseHolder> holders = adapter.getHolders();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            int type = adapter.getItemViewType(i);
            ExtraFormData extra = new ExtraFormData();
            EditFormElementAdapter.BaseHolder baseHolder = holders.get(i);
            if (baseHolder == null) {
                Log.d("GIASFELFEBREHBER3", "TRUE: " + i); continue;}

            // TextField
            if (type == 1) {
                EditFormElementAdapter.FieldViewHolder holder = (EditFormElementAdapter.FieldViewHolder) baseHolder;
                extra.setCharLimit(holder.getLimit());
                extra.setValidatorClass(holder.getValidator());
            }
            // Spinner
            else if (type == 0) {
                EditFormElementAdapter.SpinnerViewHolder holder = (EditFormElementAdapter.SpinnerViewHolder) baseHolder;
                extra.setElements(holder.getElements());
            }

            elements.get(i).setLabel(baseHolder.getName());
            elements.get(i).setExtra(extra);
        }
        service.setName(nameField.getText().toString());
    }
}