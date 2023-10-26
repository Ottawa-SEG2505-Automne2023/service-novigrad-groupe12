package com.example.servicenovigrad.ui;

import android.os.Bundle;

import com.example.servicenovigrad.R;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SuccessfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucessful_login);
        handleUsername();
    }
    public void handleUsername(){
        EditText t= findViewById(R.id.loginUsernameInput);
        String input = "Bienvenue, " + t.getText().toString();
        ((TextView)findViewById(R.id.textView4)).setText(input);
        Log.d("Username", input);
    }
}
