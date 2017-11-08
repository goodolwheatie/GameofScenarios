package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Spinner spinnerGender, spinnerEthnicity, spinnerAge;
    private EditText editTextEmail, editTextPassword, editUsername;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private void handleGenderSpinner(){
        // create spinner or dropdown box class
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }

    private void handleEthnicitySpinner(){
        // create spinner or dropdown box class
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.ethnicity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEthnicity.setAdapter(adapter);
    }

    private void handleAgeSpinner(){
        // create spinner or dropdown box class
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(adapter);
    }

    private void actionBarSetup() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Register");
        ab.setSubtitle("Create your profile");
    }

    private void writeOptionalProfile(String username){
        String textGender = spinnerGender.getSelectedItem().toString();
        String textAge = spinnerAge.getSelectedItem().toString();
        String textEthnicity = spinnerEthnicity.getSelectedItem().toString();

        if (textGender.equals("Select Option..")){
            textGender = "";
        }
        if (textAge.equals("Select Option..")){
            textAge = "";
        }
        if (textEthnicity.equals("Select Option..")){
            textEthnicity = "";
        }

        User currentUser = new User(username, textGender, textAge, textEthnicity);
    }

    public void registerUser(View v) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String username = editUsername.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        if (username.isEmpty()){
            editUsername.setError("Username is required");
            editUsername.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void init()
    {
        editTextEmail = findViewById(R.id.etCreateEmail);
        editTextPassword = findViewById(R.id.etCreatePW);
        editUsername = findViewById(R.id.etCreateUser);

        spinnerGender = findViewById(R.id.spnGender);
        spinnerEthnicity = findViewById(R.id.spnEthn);
        spinnerAge = findViewById(R.id.spnAge);

        progressBar = (ProgressBar) findViewById(R.id.pbRegister);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // initialize actionbar title
        actionBarSetup();

        // initialize drop box for gender
        handleGenderSpinner();
        handleAgeSpinner();
        handleEthnicitySpinner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize activity
        init();

    }
}
