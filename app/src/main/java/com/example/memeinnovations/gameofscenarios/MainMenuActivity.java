package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class MainMenuActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private boolean anonymousUser = false;
    Button btnProfileAndReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // initialize profile button so that the text can be changed for anon
        btnProfileAndReg = findViewById(R.id.btnProfile);

        // get if anonymous has logged in or not
        Bundle bundles = getIntent().getExtras();
        if (bundles != null) {
            anonymousUser = bundles.getBoolean("anonymousUser");
            if (anonymousUser) {
                btnProfileAndReg.setText("Register Now");
            }
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("prisoners_dilemma").child("Room 1");
        mAuth = FirebaseAuth.getInstance();
    }

    public void play(View view){
        Intent openGameLobby = new Intent(MainMenuActivity.this, GameLobbyActivity.class);
        startActivity(openGameLobby);
    }

    public void rules(View view){
        Intent openRules = new Intent(MainMenuActivity.this, RulesActivity.class);
        startActivity(openRules);
    }

    public void profile(View view){
        Intent openActivity;
        if (anonymousUser){
            // open register for anonymous users
            openActivity = new Intent (MainMenuActivity.this, RegisterActivity.class);
        }
        else {
            // open profile for already logged in users
            openActivity = new Intent(MainMenuActivity.this, ProfileActivity.class);
        }
        startActivity(openActivity);
    }

    public void statistics(View view){
        Intent openStatistics = new Intent(MainMenuActivity.this, StatisticsActivity.class);
        startActivity(openStatistics);
    }
}
