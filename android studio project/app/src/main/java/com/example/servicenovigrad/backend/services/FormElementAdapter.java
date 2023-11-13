package com.example.servicenovigrad.backend.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.util.Callable;
import com.example.servicenovigrad.ui.admin.EditFormActivity;

import java.util.List;

public class FormElementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final EditFormActivity context;
    private List<FormElement> elements;
    private int currPos = 0;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class FieldViewHolder extends RecyclerView.ViewHolder {
        private final TextView namePrompt;
        private final EditText nameField;
        private final EditText limitField;
        private final Spinner validatorSpinner;
        private final Button del;
        private int pos;
        public FieldViewHolder(View view, int pos) {
            super(view);
            this.pos = pos;
            namePrompt = view.findViewById(R.id.fieldNamePrompt);
            nameField = view.findViewById(R.id.fieldNameField);
            limitField = view.findViewById(R.id.charLimitField);
            validatorSpinner = view.findViewById(R.id.validatorSpinner);
            del = view.findViewById(R.id.deleteButton);
        }

        public String getName() {return nameField.getText().toString();}
        public int getLimit() {return Integer.parseInt(limitField.getText().toString());}
        public int getValidator() {return validatorSpinner.getSelectedItemPosition();}
        public void setDeleteMethod(Callable method) {
            del.setOnClickListener(v -> method.call(null));
        }
    }

    public static class SpinnerViewHolder extends RecyclerView.ViewHolder {
        public SpinnerViewHolder(View view) {
            super(view);
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param context EditFormActivity containing the data to populate views to be used
     * @param elements List<FormElement> containing the data to populate views to be used
     * by RecyclerView
     */
    public FormElementAdapter(EditFormActivity context, List<FormElement> elements) {
        this.context = context;
        this.elements = elements;
    }

    @Override
    public int getItemViewType(int position) {
        // SPINNERS = 0
        // FIELDS = 1
        if (elements.get(position).getType() == ElementType.SPINNER) {
            return 0;
        }
        return 1;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item

        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.field_element_editor_layout, viewGroup, false);
            FieldViewHolder holder = new FieldViewHolder(view, currPos++);
            // holder.setDeleteMethod(v -> {context.compileForm(); elements.remove(); context.refreshDisplay();});
            return holder;
        }
        else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_element_editor_layout, viewGroup, false);
            return new SpinnerViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        FormElement elem = elements.get(position);
        if (elem.getType() == ElementType.TEXTFIELD) {
            FieldViewHolder holder = (FieldViewHolder) viewHolder;

            String nameText = "Nom (" + elem.getType() + ")";
            holder.namePrompt.setText(nameText);

            holder.nameField.setText(elem.getLabel());

            ExtraFormData extra = elem.getExtra();
            if (extra != null) {
                holder.limitField.setText(String.valueOf(extra.getCharLimit()));
                holder.validatorSpinner.setSelection(extra.getValidatorClass());
            } else {
                holder.limitField.setText(String.valueOf(15));
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return elements.size();
    }
}
