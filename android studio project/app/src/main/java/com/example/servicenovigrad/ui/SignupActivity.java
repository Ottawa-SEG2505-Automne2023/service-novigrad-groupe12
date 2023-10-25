package com.example.servicenovigrad;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class SignupActivity extends AppCompatActivity {
    EditText signUpName, signUpusername, signUpemail,signUppassword, role;
    TextView loginRedirect;
    Button Signup;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference newUserRoleRef = database.getReference("users/" + signUpName.getText() + "/role");
        DatabaseReference newUserEmailRef = database.getReference("users/" + signUpemail.getText() + "/email");
        DatabaseReference newUserPasswordRef = database.getReference("users/" + signUppassword.getText() + "/password");

        newUserRoleRef.setValue(role);
        newUserEmailRef.setValue(signUpemail);
        newUserPasswordRef.setValue(signUppassword);
    }
}