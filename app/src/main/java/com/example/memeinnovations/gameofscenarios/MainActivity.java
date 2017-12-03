package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    private boolean anonymousUser;

    private void anonLogin(){
        mAuth.signInAnonymously().
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void userLogin()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Username is required");
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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

        }
        });
    }

    private void init() {
        editTextEmail = (EditText) findViewById(R.id.etEmail);
        editTextPassword = (EditText) findViewById(R.id.etPass);
        progressBar = (ProgressBar) findViewById(R.id.pbMain);
        mAuth = FirebaseAuth.getInstance();

        Button btnLogin, btnAnon, btnReg;
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnAnon = (Button) findViewById(R.id.btnAnonymously);
        btnReg = (Button) findViewById(R.id.btnRegister);
        btnAnon.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnRegister:
                Intent openRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(openRegister);
                break;
            case R.id.btnAnonymously:
                anonymousUser = true;
                Intent openMainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
                openMainMenu.putExtra("anonymousUser", anonymousUser);
                startActivity(openMainMenu);
                break;
            case R.id.btnLogin:
                userLogin();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user already logged in
        if (mAuth.getCurrentUser() != null) {
            Intent openRegister = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(openRegister);
            finish();
        }
        init();
    }


}
