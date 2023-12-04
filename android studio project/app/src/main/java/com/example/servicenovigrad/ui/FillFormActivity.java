package com.example.servicenovigrad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.services.EditFormElementAdapter;
import com.example.servicenovigrad.backend.services.FilledForm;
import com.example.servicenovigrad.backend.services.FormElement;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.util.validators.FieldValidator;
import com.example.servicenovigrad.ui.admin.EditFormActivity;

import java.util.List;

// Currently a placeholder
public class FillFormActivity extends AppCompatActivity {
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
            public void setCharLimit(int limit) {/* TODO: set the character limit */}
            public void setValidator(FieldValidator validator) {/* TODO: add a field validator */}
            public String getText() {return field.getText().toString();}
        }
        private static class SpinnerHolder extends FilledBaseHolder {
            private final Spinner spinner;
            public SpinnerHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.spinnerName); spinner = itemView.findViewById(R.id.spinner);}
            public void setElements(List<String> elements) {/* TODO: modify contents of the spinner */}
            public String getText() {return spinner.getSelectedItem().toString();}
        }
        private static class DocumentHolder extends FilledBaseHolder {
            public DocumentHolder(@NonNull View itemView) {super(itemView); name = itemView.findViewById(R.id.documentName);}
            public String getText() {return context.getContext().getString(R.string.document_disclaimer);}
        }

        private final FillFormActivity context;
        private final List<FormElement> elements;
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
            // TODO: Bind data from the FormElement to the ViewHolder
        }

        @Override
        public int getItemCount() {
            return elements.size();
        }
    }

    private ServiceForm service;
    private RecyclerView elementList;
    private Button submitBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        // Finding the service that was opened
        String id = getIntent().getStringExtra("formID");
        for (ServiceForm s : DatabaseHandler.getServicesList()) {
            if (s.getId().equals(id)) {
                service = s;
            }
        }

        // Setting the activity title
        TextView serviceTitle = findViewById(R.id.serviceTitle);
        String title = "Demande: " + service.getName();
        serviceTitle.setText(title);

        // TODO: make these elements functional
        elementList = findViewById(R.id.elementList);
        submitBtn = findViewById(R.id.submitBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
    }
}