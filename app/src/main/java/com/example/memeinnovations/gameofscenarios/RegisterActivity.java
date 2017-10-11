package com.example.memeinnovations.gameofscenarios;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    private void handleGenderSpinner(){
        // create spinner or dropdown box class
        Spinner spinnerGender = (Spinner) findViewById(R.id.spnGender);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }
    private void actionBarSetup() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Register");
        ab.setSubtitle("Create your profile");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize actionbar title
        actionBarSetup();

        // initialize drop box for gender
        handleGenderSpinner();
    }
}
