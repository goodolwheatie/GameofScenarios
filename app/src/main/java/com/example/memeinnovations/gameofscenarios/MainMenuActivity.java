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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("prisoners_dilemma").child("Room 1");
        mAuth = FirebaseAuth.getInstance();
    }

    public void connect(){
        String userID = mAuth.getCurrentUser().getUid();
        String player1;

        // setting data from database
        // mDatabase.child("gender").setValue(currGen);

        // reading data from database
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean temp = (boolean) dataSnapshot.child("in_use").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    public void play(View view){
        Intent openGameLobby = new Intent(MainMenuActivity.this, GameLobbyActivity.class);
        startActivity(openGameLobby);
    }

    public void profile(View view){
        Intent openProfile = new Intent(MainMenuActivity.this, ProfileActivity.class);
        startActivity(openProfile);
    }
}
