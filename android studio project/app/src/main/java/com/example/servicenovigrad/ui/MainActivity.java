package com.example.servicenovigrad.ui;

import android.os.Bundle;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.account.CompleteBranch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WelcomeActivity {

    private List<CompleteBranch> completeBranches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.mainWelcomeText);
        List<CompleteBranch> filteredBranches = searchBranchByRating(3.5); // Replace 3.5 with your desired minimum rating
    }

    private List<CompleteBranch> searchBranchByRating(double minRating) {
        List<CompleteBranch> filteredBranches = new ArrayList<>();

        for (CompleteBranch branch : completeBranches) {
            double rating = branch.rating();

            // Handle the special case when there are currently no ratings (-1)
            if (rating != -1 && rating >= minRating) {
                filteredBranches.add(branch);
            }
        }

        return filteredBranches;
    }
}
