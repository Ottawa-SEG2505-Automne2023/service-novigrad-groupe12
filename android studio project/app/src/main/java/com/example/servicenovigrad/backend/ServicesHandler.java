package com.example.servicenovigrad.backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServicesHandler {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference servicesRef = database.getReference("globalServices");

    public static void addService(ServiceForm s) {
        DatabaseReference root = servicesRef.child(s.getName().trim());
        root.child("name").setValue(s.getName());
        int i = 0;
        for (Object elem : s.getElements()) {
            root.child("elements").child(String.valueOf(i)).setValue(elem);
        }
        root.child("elements").setValue(s.getElements());
    }
}
