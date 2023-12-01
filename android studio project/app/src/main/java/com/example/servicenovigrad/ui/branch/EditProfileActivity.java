package com.example.servicenovigrad.ui.branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.BranchAccount;
import com.example.servicenovigrad.backend.account.CompleteBranch;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.util.Updatable;
import com.example.servicenovigrad.backend.util.validators.AddressValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements Updatable {
    // Adapter for the ListView
    private static class BranchServiceAdapter extends ArrayAdapter<ServiceForm> {
        private final AppCompatActivity context;
        private final List<ServiceForm> services;
        private final HashMap<String, Boolean> serviceToggles = new HashMap<>();
        public BranchServiceAdapter(@NonNull AppCompatActivity context, List<ServiceForm> services) {
            super(context, R.layout.employee_service_checker, services);
            this.context = context;
            this.services = services;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.employee_service_checker, null, true);

            ServiceForm service = services.get(position);

            TextView serviceName = listViewItem.findViewById(R.id.serviceName);
            serviceName.setText(service.getName());

            RadioButton trueButton = listViewItem.findViewById(R.id.trueButton);
            RadioButton falseButton = listViewItem.findViewById(R.id.falseButton);

            falseButton.toggle();
            serviceToggles.put(service.getId(), false);

            // Toggle on if this service is offered by the given branch
            BranchAccount user = (BranchAccount) DatabaseHandler.user;
            if (user.getServiceMap().containsKey(service.getId())) {
                if (Boolean.TRUE.equals(user.getServiceMap().get(service.getId()))) {
                    trueButton.toggle();
                    serviceToggles.put(service.getId(), true);
                }
            }

            trueButton.setOnClickListener(v -> serviceToggles.put(service.getId(), true));
            falseButton.setOnClickListener(v -> serviceToggles.put(service.getId(), false));

            return listViewItem;
        }
        public HashMap<String, Boolean> getServiceToggles() {
            return serviceToggles;
        }
    }
    private final List<CheckBox> dayBoxes = new ArrayList<>();
    private Spinner openingSpinner;
    private Spinner closingSpinner;
    private ListView serviceList;
    private TextView addressPrompt;
    private EditText addressField;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        BranchAccount user = (BranchAccount) DatabaseHandler.user;

        dayBoxes.add(findViewById(R.id.monBt));
        dayBoxes.add(findViewById(R.id.tueBt));
        dayBoxes.add(findViewById(R.id.wedBt));
        dayBoxes.add(findViewById(R.id.thuBt));
        dayBoxes.add(findViewById(R.id.friBt));
        dayBoxes.add(findViewById(R.id.satBt));
        dayBoxes.add(findViewById(R.id.sunBt));
        openingSpinner = findViewById(R.id.openTimeSpinner);
        closingSpinner = findViewById(R.id.closingTimeSpinner);
        serviceList = findViewById(R.id.servicesList);
        addressPrompt = findViewById(R.id.addressPrompt);
        addressField = findViewById(R.id.addressText);
        save = findViewById(R.id.saveProfile);

        // Fill out available information
        for (int i = 0; i < user.getDaysList().size(); i++) {
            dayBoxes.get(i).setChecked(user.getDaysList().get(i));
        }

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                update();
            }
        };

        openingSpinner.setSelection(user.getOpeningHours());
        openingSpinner.setOnItemSelectedListener(spinnerListener);

        closingSpinner.setSelection(user.getClosingHours());
        closingSpinner.setOnItemSelectedListener(spinnerListener);

        serviceList.setAdapter(new BranchServiceAdapter(this, DatabaseHandler.getServicesList()));

        addressField.addTextChangedListener(new AddressValidator(this, addressPrompt, "Adresse"));
        if (user.getAddress() != null) {addressField.setText(user.getAddress());}

        save.setOnClickListener(v -> saveProfile());
        update();
    }

    public void update() {
        save.setEnabled(addressPrompt.getTextColors().getDefaultColor() != 0xFFFF0000 && addressField.getText().length() > 0 && openingSpinner.getSelectedItemPosition() < closingSpinner.getSelectedItemPosition());
    }

    private void saveProfile() {
        BranchAccount user = (BranchAccount) DatabaseHandler.user;

        // Get the days
        ArrayList<Boolean> days = new ArrayList<>();
        for (CheckBox b : dayBoxes) {days.add(b.isChecked());}
        user.setDaysList(days);

        // Get the hours of operation
        user.setOpeningHours(openingSpinner.getSelectedItemPosition());
        user.setClosingHours(closingSpinner.getSelectedItemPosition());

        // Get the input address
        user.setAddress(addressField.getText().toString());

        // Get the list of offered services
        user.setServiceMap(((BranchServiceAdapter) serviceList.getAdapter()).getServiceToggles());

        // Upload to database & close this activity
        DatabaseHandler.getDatabase().getReference("users/" + user.getUsername()).setValue(user);

        // Upload to a new directory of the database, for users to be able to search from
        DatabaseHandler.getDatabase().getReference("completeBranches/" + user.getUsername()).setValue(new CompleteBranch(user));

        finish();
    }
}