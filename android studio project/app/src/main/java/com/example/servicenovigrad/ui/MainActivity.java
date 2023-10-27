package com.example.servicenovigrad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.backend.AccountHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference nameRef = database.getReference("users/" + AccountHandler.user + "/prenom");
        DatabaseReference roleRef = database.getReference("users/" + AccountHandler.user + "/role");

        ArrayList<String> strList = new ArrayList<>();
        strList.add("Bienvenue ");

        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strList.add(snapshot.getValue(String.class) + "! Vous êtes connecté en tant que ");
                // Log.d("A", strList.toString());
                nameRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error, would've been detected earlier
            }
        });

        // Log.d("B", strList.toString());

        roleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strList.add(snapshot.getValue(String.class) + ".");
                // Log.d("C", strList.toString());
                setWelcomeText(strList);
                roleRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Log.d("D", strList.toString());
    }

    private void setWelcomeText(ArrayList<String> arr) {
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);

        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += arr.get(i);
        }
        welcomeText.setText(str);
    }
}