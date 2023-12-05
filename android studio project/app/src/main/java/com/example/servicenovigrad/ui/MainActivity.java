package com.example.servicenovigrad.ui;

import android.os.Bundle;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.DatabaseHandler;
import com.example.servicenovigrad.backend.account.CompleteBranch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WelcomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.mainWelcomeText);
        List<CompleteBranch> filteredBranches = searchBranch(3.5, "123 Main St", 14, "Document Processing");
    }

    private List<CompleteBranch> searchBranch(double minRating, String addressSearchTerm, int workHours, String serviceType) {
        List<CompleteBranch> filteredBranches = new ArrayList<>();

        for (CompleteBranch branch : DatabaseHandler.getBranches()) {
            double rating = branch.rating();

            // Check if the branch meets the rating criteria
            if (rating != -1 && rating >= minRating) {
                // Check if the branch has the desired service type
                if (serviceType == null || branch.hasService(serviceType)) {
                    // Check if the branch is open at the specified work hours
                    if (workHours == -1 || branch.isOpenAt(workHours)) {
                        // Check if the branch has the specified address
                        if (addressSearchTerm == null || branch.hasAddress(addressSearchTerm)) {
                            filteredBranches.add(branch);
                        }
                    }
                }
            }
        }

        return filteredBranches;
    }
}
