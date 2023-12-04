package com.example.servicenovigrad.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.servicenovigrad.R;

import android.content.Intent;

import android.view.View;

import android.widget.Button;

import android.widget.LinearLayout;


public class FillFormActivity extends AppCompatActivity {


    private ServiceForm currentForm; // The current form being filled out
    private final Map<FormElement, View> formElements = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        // Retrieve the service form ID from the intent and load the form
        String formId = getIntent().getStringExtra("formID");
        currentForm = getServiceForm(formId); // retrieves the form

        buildForm(currentForm);
    }

    private void buildForm(ServiceForm serviceForm) {
        LinearLayout formLayout = findViewById(R.id.formLayout);
        formLayout.removeAllViews();
    
        for (FormElement element : serviceForm.getElements()) {
            View view = null;
    
            switch (element.getType()) {
                case TEXTFIELD:
                    EditText editText = new EditText(this);
                    editText.setHint(element.getLabel());
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    if (element.getExtra() != null && element.getExtra().getCharLimit() > 0) {
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(element.getExtra().getCharLimit())});
                    }
                    view = editText;
                    formData.put(element, editText);
                    break;
                case SPINNER:
                    Spinner spinner = new Spinner(this);
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, element.getExtra().getElements());
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);
                    view = spinner;
                    formData.put(element, spinner);
                    break;
                case DOCUMENT:
                    Button uploadButton = new Button(this);
                    uploadButton.setText(element.getLabel());
                    uploadButton.setOnClickListener(v -> selectFile(element));
                    view = uploadButton;
                    formData.put(element, null);//placeholder
                    break;
            }
    
            if (view != null) {
                formLayout.addView(view);
            }
        }

            // Add a submit button at the end of the form
            Button submitButton = new Button(this);
            submitButton.setText("Submit");
            submitButton.setOnClickListener(v -> submitForm());
            formLayout.addView(submitButton);
    }

    private static final int FILE_SELECT_CODE = 0;

    private void selectFile(FormElement element) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

    private FormElement currentSelectingElement;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            // Store the URI of the selected file in formData
            formData.put(currentSelectingElement, data.getData());
        }
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

    private void submitForm() {
        // Iterate through formData to collect and process data
        for (Map.Entry<FormElement, Object> entry : formData.entrySet()) {
            FormElement element = entry.getKey();
            Object value = entry.getValue();
            
            // Process each form element based on its type
            // This can be expanded to handle each type as needed
            switch (element.getType()) {
                case TEXTFIELD:
                    String textValue = ((EditText)value).getText().toString();
                    // Process text field data
                    break;
                case SPINNER:
                    String spinnerValue = ((Spinner)value).getSelectedItem().toString();
                    // Process spinner data
                    break;
                case DOCUMENT:
                    // Process document data
                    break;
            }
        }

    }


    
    
}