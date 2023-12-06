package com.example.servicenovigrad.ui.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.Objects;
import java.util.TreeMap;

public class EditFormActivity extends AppCompatActivity implements Updatable {
    private RecyclerView list;
    private Spinner typeSpinner;
    private Button saveButton;
    private EditText nameField;
    private TextView namePrompt;
    private ServiceForm service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_form);

        // Get things
        list = findViewById(R.id.elementList);
        typeSpinner = findViewById(R.id.typeSpinner);
        Button addButton = findViewById(R.id.addElementButton);
        saveButton = findViewById(R.id.saveServiceButton);
        Button deleteButton = findViewById(R.id.deleteServiceButton);
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

            // Refresh the display (scroll to bottom to ensure holder is properly binded and added to TreeMap)
            list.smoothScrollToPosition(service.getElements().size());
            Objects.requireNonNull(list.getAdapter()).notifyItemInserted(service.getElements().size());
        });

        // Saving the form to the database
        saveButton.setOnClickListener(v -> {compileForm(); DatabaseHandler.addService(service); finish();});

        // Deleting the form from the database
        deleteButton.setOnClickListener(v -> showFormDeletionConfirmation(service));

        // Visual stuff for the RecyclerView
        list.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration spacer = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        Objects.requireNonNull(spacer.getDrawable()).setColorFilter(0xFF000000, PorterDuff.Mode.SRC_OVER);
        list.addItemDecoration(spacer);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set up the display
        list.setAdapter(new EditFormElementAdapter(this, service.getElements()));
    }

    @Override
    public void update() {
        // Update the save button
        saveButton.setEnabled(namePrompt.getTextColors().getDefaultColor() != 0xFFFF0000);
    }

    public void compileForm() {
        EditFormElementAdapter adapter = (EditFormElementAdapter) list.getAdapter();
        List<FormElement> elements = service.getElements();
        assert adapter != null;
        TreeMap<FormElement, EditFormElementAdapter.BaseHolder> holders = adapter.getHolders();

        // Save each element
        for (int i = 0; i < holders.size(); i++) {
            compileElement(adapter, holders.get(elements.get(i)), i);
        }
        service.setName(nameField.getText().toString());
    }

    public void compileElement(EditFormElementAdapter adapter, EditFormElementAdapter.BaseHolder baseHolder, int position) {
        // Only compile the element if the holder isn't recycled
        if (adapter.isEnabled(position)) {
            FormElement elem = service.getElements().get(position);
            ExtraFormData extra = new ExtraFormData();

            // TextField
            if (baseHolder instanceof EditFormElementAdapter.FieldViewHolder) {
                EditFormElementAdapter.FieldViewHolder holder = (EditFormElementAdapter.FieldViewHolder) baseHolder;
                extra.setCharLimit(holder.getLimit());
                extra.setValidatorClass(holder.getValidator());
            }
            // Spinner
            else if (baseHolder instanceof EditFormElementAdapter.SpinnerViewHolder) {
                EditFormElementAdapter.SpinnerViewHolder holder = (EditFormElementAdapter.SpinnerViewHolder) baseHolder;
                extra.setElements(holder.getElements());
            }

            elem.setLabel(baseHolder.getName());
            elem.setExtra(extra);
        }
    }

    // Very similar to AdminAccountManageActivity's showAccountDeletionConfirmation
    public void showFormDeletionConfirmation(ServiceForm s) {
        // Creating the layout inflater
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_account_deletion, null);
        dialogBuilder.setView(dialogView);

        // Setting layout-specific values
        TextView prompt = dialogView.findViewById(R.id.textView4);
        prompt.setText(R.string.admin_confirm_delete_form);

        TextView accountDisplay = dialogView.findViewById(R.id.accountDisplayText);
        String accountInfo = s.getName();
        accountDisplay.setText(accountInfo);

        Button yes = dialogView.findViewById(R.id.confirmDelete);
        Button no  = dialogView.findViewById(R.id.declineDelete);

        // Show the warning dialog
        dialogBuilder.setTitle("Confirmation");
        AlertDialog b = dialogBuilder.create();
        b.show();

        // Button event listeners
        yes.setOnClickListener(v -> {
            DatabaseHandler.deleteService(s);
            b.dismiss();
            finish();
        });

        no.setOnClickListener(v -> b.dismiss());
    }
}