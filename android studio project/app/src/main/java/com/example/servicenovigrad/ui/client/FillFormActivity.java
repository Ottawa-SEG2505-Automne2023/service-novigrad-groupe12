package com.example.servicenovigrad.ui.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.services.FilledForm;
import com.example.servicenovigrad.backend.services.FormElement;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.util.Updatable;
import com.example.servicenovigrad.backend.util.validators.AddressValidator;
import com.example.servicenovigrad.backend.util.validators.FieldValidator;
import com.example.servicenovigrad.backend.util.validators.NameValidator;
import com.example.servicenovigrad.backend.util.validators.NumberValidator;
import com.example.servicenovigrad.backend.util.validators.OldDateValidator;
import com.example.servicenovigrad.backend.util.validators.UserPassValidator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FillFormActivity extends AppCompatActivity implements Updatable {
    // Adapter for the form filler
    private static class FillFormElementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // Base holder
        private static abstract class FilledBaseHolder extends RecyclerView.ViewHolder {
            protected View context;
            protected TextView name;
            public FilledBaseHolder(@NonNull View itemView) {super(itemView); context = itemView;}
            public void setName(String name) {this.name.setText(name);}
            public String getName() {return name.getText().toString();}
            public boolean isValid() {return name.getTextColors().getDefaultColor() != 0xFFFF0000;}
            public abstract String getText();
        }
        // Holder for TextFields
        private static class FieldHolder extends FilledBaseHolder {
            private final EditText field;
            public FieldHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.fieldName); field = itemView.findViewById(R.id.field);}
            public void setCharLimit(int limit) {field.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});}
            public void setValidator(FieldValidator validator) {field.addTextChangedListener(validator);}
            public String getText() {return field.getText().toString();}
        }
        // Holder for Spinners
        private static class SpinnerHolder extends FilledBaseHolder {
            private final Spinner spinner;
            public SpinnerHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.spinnerName); spinner = itemView.findViewById(R.id.spinner);}
            public void setElements(List<String> elements) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context.getContext(), android.R.layout.simple_spinner_item, elements);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
            }
            public String getText() {return spinner.getSelectedItem().toString();}
        }
        // Holder for documents
        private static class DocumentHolder extends FilledBaseHolder {
            public DocumentHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.documentName);}
            public String getText() {return context.getContext().getString(R.string.document_disclaimer);}
        }

        private final FillFormActivity context;
        private final List<FormElement> elements;
        private final List<FilledBaseHolder> holders = new ArrayList<>();
        public FillFormElementAdapter(FillFormActivity context, List<FormElement> elements) {
            this.context = context;
            this.elements = elements;
        }

        @Override
        // Literally copy-pasted from EditFormElementAdapter
        public int getItemViewType(int position) {
            // SPINNERS = 0
            // FIELDS = 1
            // DOCUMENTS = 2
            switch (elements.get(position).getType()) {
                case SPINNER:
                    return 0;
                case DOCUMENT:
                    return 2;
                default:
                    // TEXTFIELD
                    return 1;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view;
            if (viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_element_layout, parent, false);
                return new FillFormElementAdapter.SpinnerHolder(view);
            }
            if (viewType == 1) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_element_layout, parent, false);
                return new FillFormElementAdapter.FieldHolder(view);
            }
            else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_element_layout, parent, false);
                return new FillFormElementAdapter.DocumentHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            FilledBaseHolder baseHolder = (FilledBaseHolder) holder;
            FormElement element = elements.get(position);

            // Setting all the specified variables accordingly
            baseHolder.setName(element.getLabel());
            switch (element.getType()) {
                case TEXTFIELD:
                    FieldHolder fieldHolder = (FieldHolder) baseHolder;
                    fieldHolder.setCharLimit(element.getExtra().getCharLimit());
                    fieldHolder.setValidator(getValidator(element.getExtra().getValidatorClass(), fieldHolder.name, fieldHolder.getName()));
                    break;
                case SPINNER:
                    SpinnerHolder spinnerHolder = (SpinnerHolder) baseHolder;
                    spinnerHolder.setElements(element.getExtra().getElements());
                    break;
                default:
                    break;
            }
            holders.add(baseHolder);
        }

        @Override
        public int getItemCount() {
            return elements.size();
        }

        public List<FilledBaseHolder> getHolders() {return holders;}

        // Creates a FieldValidator based off of the stored ValidatorID in the element's ExtraFormData
        private FieldValidator getValidator(int validatorID, TextView label, String type) {
            switch (validatorID) {
                case 0:
                    return new FieldValidator(context, label, type);
                case 1:
                    return new UserPassValidator(context, label, type);
                case 2:
                    return new NameValidator(context, label, type);
                case 3:
                    return new AddressValidator(context, label, type);
                case 4:
                    return new OldDateValidator(context, label, type, 18);
                default:
                    return new NumberValidator(context, label, type);
            }
        }
    }

    private ServiceForm service;
    private String target;
    private FillFormElementAdapter adapter;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        // Finding the service that was opened & the targeted branch
        String id = getIntent().getStringExtra("formID");
        for (ServiceForm s : DatabaseHandler.getServicesList()) {
            if (s.getId().equals(id)) {
                service = s;
            }
        }
        target = getIntent().getStringExtra("target");

        // Making Views useful
        TextView serviceTitle = findViewById(R.id.serviceTitle);
        String title = "Demande: " + service.getName();
        serviceTitle.setText(title);

        RecyclerView elementList = findViewById(R.id.elementList);
        elementList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration spacer = new DividerItemDecoration(elementList.getContext(), DividerItemDecoration.VERTICAL);
        Objects.requireNonNull(spacer.getDrawable()).setColorFilter(0xFF000000, PorterDuff.Mode.SRC_OVER);
        elementList.addItemDecoration(spacer);
        adapter = new FillFormElementAdapter(this, service.getElements());
        elementList.setAdapter(adapter);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> {submitForm(); finish();});
        submitBtn.setEnabled(false);

        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> finish());
    }

    // Update whether or not we can submit the form in its current state
    // Commented out because it's laggy as hell
    public void update() {
        /*
        for (FillFormElementAdapter.FilledBaseHolder holder : adapter.getHolders()) {
            if (holder.getText().equals("")) {submitBtn.setEnabled(false); break;}
            else if (!holder.isValid()) {submitBtn.setEnabled(false); break;}
        }
         */
        submitBtn.setEnabled(true);
    }

    public void submitForm() {
        // Creating & filling the FilledForm
        FilledForm formToSubmit = new FilledForm();
        formToSubmit.setSource(DatabaseHandler.user);
        formToSubmit.setName(service.getName());
        List<String> text = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            text.add(adapter.getHolders().get(i).getName());
            text.add(adapter.getHolders().get(i).getText());
        }
        formToSubmit.setTextSequence(text);

        // Push to database
        DatabaseReference ref = DatabaseHandler.getDatabase().getReference("users/" + target + "/requests");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.child(""+snapshot.getChildrenCount()).getRef().setValue(formToSubmit);
                } else {
                    snapshot.child("0").getRef().setValue(formToSubmit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SERVICE PUSH ERROR", "Couldn't connect to database: " + error.getMessage());
            }
        });
    }
}