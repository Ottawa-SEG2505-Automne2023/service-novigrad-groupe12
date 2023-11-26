package com.example.servicenovigrad.ui.branch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.servicenovigrad.R;

public class EditProfileActivity extends AppCompatActivity {
    private final CheckBox[] dayBoxes = new CheckBox[7];
    private Spinner openingSpinner;
    private Spinner closingSpinner;
    private ListView serviceList;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dayBoxes[0] = findViewById(R.id.monBt);
        dayBoxes[1] = findViewById(R.id.tueBt);
        dayBoxes[2] = findViewById(R.id.wedBt);
        dayBoxes[3] = findViewById(R.id.thuBt);
        dayBoxes[4] = findViewById(R.id.friBt);
        dayBoxes[5] = findViewById(R.id.satBt);
        dayBoxes[6] = findViewById(R.id.sunBt);
        openingSpinner = findViewById(R.id.openTimeSpinner);
        closingSpinner = findViewById(R.id.closingTimeSpinner);
        serviceList = findViewById(R.id.servicesList);
        save = findViewById(R.id.saveProfile);
    }
}