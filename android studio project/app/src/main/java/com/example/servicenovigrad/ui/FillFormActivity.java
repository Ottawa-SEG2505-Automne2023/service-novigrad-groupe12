package com.example.servicenovigrad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputFilter;
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
import com.example.servicenovigrad.backend.util.validators.FieldValidator;
import com.example.servicenovigrad.backend.util.validators.NameValidator;
import com.example.servicenovigrad.backend.util.validators.NumberValidator;
import com.example.servicenovigrad.backend.util.validators.OldDateValidator;
import com.example.servicenovigrad.backend.util.validators.UserPassValidator;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

// Currently a placeholder
public class FillFormActivity extends AppCompatActivity implements Updatable {
    private static class FillFormElementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static abstract class FilledBaseHolder extends RecyclerView.ViewHolder {
            protected View context;
            protected TextView name;
            public FilledBaseHolder(@NonNull View itemView) {super(itemView); context = itemView;}
            public void setName(String name) {this.name.setText(name);}
            public String getName() {return name.getText().toString();}
            public abstract String getText();
        }
        private static class FieldHolder extends FilledBaseHolder {
            private final EditText field;
            public FieldHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.fieldName); field = itemView.findViewById(R.id.field);}
            public void setCharLimit(int limit) {field.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});}
            public void setValidator(FieldValidator validator) {field.addTextChangedListener(validator);}
            public String getText() {return field.getText().toString();}
        }
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
        private static class DocumentHolder extends FilledBaseHolder {
            public DocumentHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.documentName);}
            public String getText() {return context.getContext().getString(R.string.document_disclaimer);}
        }

        private final FillFormActivity context;
        private final List<FormElement> elements;
        private final HashMap<FormElement, FilledBaseHolder> holders = new HashMap<>();
        private final HashMap<FormElement, Boolean> holdersEnabled = new HashMap<>();
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
            holdersEnabled.put(element, true);
        }

        @Override
        public int getItemCount() {
            return elements.size();
        }

        @Override
        public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
            context.compileElement((FilledBaseHolder) holder);
            holdersEnabled.put(elements.get(holder.getAdapterPosition()), false);
        }

        public boolean isEnabled(int pos) {
            return Boolean.TRUE.equals(holdersEnabled.get(elements.get(pos)));
        }

        public HashMap<FormElement, FilledBaseHolder> getHolders() {return holders;}

        private FieldValidator getValidator(int validatorID, TextView label, String type) {
            switch (validatorID) {
                case 0:
                    return new FieldValidator(context, label, type);
                case 1:
                    return new UserPassValidator(context, label, type);
                case 2:
                    return new NameValidator(context, label, type);
                case 3:
                    return new OldDateValidator(context, label, type, 18);
                default:
                    return new NumberValidator(context, label, type);
            }
        }
    }

    private ServiceForm service;
    private String target;
    private HashMap<Integer, String[]> compiledElems;
    private RecyclerView elementList;

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

        // Set up the hashmap
        compiledElems = new HashMap<>();
        for (int i = 0; i < service.getElements().size(); i++) {
            compiledElems.put(i, new String[2]);
        }

        // Making Views useful
        TextView serviceTitle = findViewById(R.id.serviceTitle);
        String title = "Demande: " + service.getName();
        serviceTitle.setText(title);

        elementList = findViewById(R.id.elementList);
        elementList.setAdapter(new FillFormElementAdapter(this, service.getElements()));

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> {submitForm(); finish();});

        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> finish());
    }

    public void update() {

    }

    // Compiles an element in the hashmap
    public void compileElement(FillFormElementAdapter.FilledBaseHolder holder) {
        int pos = holder.getAdapterPosition();
        if (((FillFormElementAdapter) Objects.requireNonNull(elementList.getAdapter())).isEnabled(pos)) {
            String[] arr = compiledElems.get(pos);
            assert arr != null;
            arr[0] = holder.getName();
            arr[1] = holder.getText();
        }
    }

    public void submitForm() {
        // Compiling elements
        for (FillFormElementAdapter.FilledBaseHolder holder : ((FillFormElementAdapter) Objects.requireNonNull(elementList.getAdapter())).getHolders().values()) {
            compileElement(holder);
        }

        // Creating & filling the FilledForm
        FilledForm formToSubmit = new FilledForm();
        formToSubmit.setSource(DatabaseHandler.user);
        formToSubmit.setName(service.getName());
        List<String> text = new ArrayList<>();
        for (int i = 0; i < compiledElems.size(); i++) {
            String[] arr = compiledElems.get(i);
            assert arr != null;
            text.add(arr[0]); text.add(arr[1]);
        }
        formToSubmit.setTextSequence(text);

        // Push to database
        DatabaseReference ref = DatabaseHandler.getDatabase().getReference("users/" + target + "/requests").push();
        ref.setValue(formToSubmit);
    }
}