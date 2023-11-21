package com.example.servicenovigrad.backend.services;

import android.annotation.SuppressLint;
import android.util.Log;
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
import com.example.servicenovigrad.backend.util.validators.NameValidator;
import com.example.servicenovigrad.ui.admin.EditFormActivity;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class EditFormElementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // All ViewHolders have a delete button
    // NOTE: name fields can be left blank on purpose!! This is by design!! DO NOT DOCK MY MARKS FOR THIS!
    // A blank name on a FormElement will simply cause the form builder in fill mode to not create a label for that element.
    // There are reasons an administrator might want this.
    public static class BaseHolder extends RecyclerView.ViewHolder {
        protected TextView namePrompt;
        protected EditText nameField;
        protected Button del;
        public BaseHolder(@NonNull View itemView) {super(itemView);}
        public void setDeleteMethod(Callable method) {
            del.setOnClickListener(v -> {del.setEnabled(false); method.call(null);});
        }

        public void fixButton() {del.setEnabled(true);}

        public String getName() {return nameField.getText().toString();}
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class FieldViewHolder extends BaseHolder {
        private final EditText limitField;
        private final Spinner validatorSpinner;
        public FieldViewHolder(View view) {
            super(view);
            namePrompt = view.findViewById(R.id.fieldNamePrompt);
            nameField = view.findViewById(R.id.fieldNameField);
            limitField = view.findViewById(R.id.charLimitField);
            validatorSpinner = view.findViewById(R.id.validatorSpinner);
            del = view.findViewById(R.id.deleteButton);
        }
        // Default 15
        public int getLimit() {
            String s = limitField.getText().toString();
            if (s.equals("")) {return 15;}
            return Integer.parseInt(s);
        }
        public int getValidator() {return validatorSpinner.getSelectedItemPosition();}
    }

    public static class SpinnerViewHolder extends BaseHolder {
        private final EditText elemsField;
        public SpinnerViewHolder(View view) {
            super(view);
            namePrompt = view.findViewById(R.id.spinnerNamePrompt);
            nameField = view.findViewById(R.id.spinnerNameField);
            TextView elemsPrompt = view.findViewById(R.id.elemsPrompt);
            elemsField = view.findViewById(R.id.elemsField);
            del = view.findViewById(R.id.deleteButton2);
        }

        // Returns the elements as a List
        public List<String> getElements() {
            String txt = elemsField.getText().toString().trim();
            txt = txt.replaceAll(", ", ",");
            String[] arr = txt.split(",");
            return Arrays.asList(arr);
        }
    }

    public static class DocumentViewHolder extends BaseHolder {
        public DocumentViewHolder(@NonNull View view) {
            super(view);
            namePrompt = view.findViewById(R.id.documentNamePrompt);
            nameField  = view.findViewById(R.id.documentNameField);
            del        = view.findViewById(R.id.deleteButton3);
        }
    }

    private final EditFormActivity context;
    private final List<FormElement> elements;
    private final TreeMap<FormElement, BaseHolder> holders = new TreeMap<>();
    private final TreeMap<FormElement, Boolean> holdersEnabled = new TreeMap<>();

    /**
     * Initialize the dataset of the Adapter
     *
     * @param elements List<FormElement> containing the data to populate views to be used
     * by RecyclerView
     */
    public EditFormElementAdapter(EditFormActivity context, List<FormElement> elements) {
        this.context = context;
        this.elements = elements;
    }

    @Override
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

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_element_editor_layout, viewGroup, false);
            return new SpinnerViewHolder(view);
        }
        if (viewType == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.field_element_editor_layout, viewGroup, false);
            return new FieldViewHolder(view);
        }
        else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_element_editor_layout, viewGroup, false);
            return new DocumentViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        FormElement elem = elements.get(position);
        BaseHolder baseHolder = (BaseHolder) viewHolder;

        // Enable the delete button & mark that this holder is not currently recycled
        baseHolder.fixButton();
        holdersEnabled.put(elem, true);

        // Update the display with data from the element
        ExtraFormData extra = elem.getExtra();

        // TextField
        if (elem.getType() == ElementType.TEXTFIELD) {
            FieldViewHolder holder = (FieldViewHolder) viewHolder;
            holders.put(elem, holder);

            if (extra != null) {
                holder.limitField.setText(String.valueOf(extra.getCharLimit()));
                holder.validatorSpinner.setSelection(extra.getValidatorClass());
            } else {
                holder.limitField.setText(String.valueOf(15));
                holder.validatorSpinner.setSelection(0);
            }
        }
        // Spinner
        else if (elem.getType() == ElementType.SPINNER) {
            SpinnerViewHolder holder = (SpinnerViewHolder) viewHolder;
            holders.put(elem, holder);

            if (extra != null) {
                String str = extra.getElements().toString();
                holder.elemsField.setText(str.substring(1, str.length() - 1));
            } else {
                holder.elemsField.setText("");
            }
        }
        // Document
        else {
            DocumentViewHolder holder = (DocumentViewHolder) viewHolder;
            holders.put(elem, holder);
        }

        // For all holder types

        // Set up the name field
        String nameText = "Nom (" + elem.getType() + ")";
        baseHolder.namePrompt.setText(nameText);
        baseHolder.nameField.setText(elem.getLabel());

        // The delete button
        baseHolder.setDeleteMethod(v -> {
            holdersEnabled.put(elem, false);
            context.compileForm();

            elements.remove(elem);
            holders.remove(elem);
            holdersEnabled.remove(elem);

            // Would technically be more efficient to use these, but I can't seem to get them to behave properly
            // notifyItemRemoved(position);
            // notifyItemRangeChanged(position, elements.size());

            notifyDataSetChanged();
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        BaseHolder baseHolder = (BaseHolder) holder;
        int pos = baseHolder.getAdapterPosition();
        if (pos >= 0) {
            context.compileElement(this, baseHolder, pos);
            holdersEnabled.put(elements.get(pos), false);
        }
    }

    public TreeMap<FormElement, BaseHolder> getHolders() {return holders;}
    // Returns false if the holder for this specific element is currently recycled
    public boolean isEnabled(int pos) {return holdersEnabled.get(elements.get(pos));}
}
