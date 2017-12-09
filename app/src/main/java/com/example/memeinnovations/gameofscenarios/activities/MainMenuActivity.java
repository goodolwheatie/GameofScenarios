package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.memeinnovations.gameofscenarios.data.FirebaseDB;
import com.example.memeinnovations.gameofscenarios.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    private boolean anonymousUser = false;
    Button btnProfileAndReg;
    TextView tvwSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // initialize profile button so that the text can be changed for anon
        btnProfileAndReg = findViewById(R.id.btnProfile);

        // set sign out text view to be clickable
        tvwSignOut = findViewById(R.id.tvwSignOut);
        tvwSignOut.setClickable(true);

        // get if anonymous has logged in or not
        Bundle bundles = getIntent().getExtras();
        if (bundles != null) {
            anonymousUser = bundles.getBoolean("anonymousUser");
            if (anonymousUser) {
                btnProfileAndReg.setText(R.string.register_now);
            }
        }
    }

    public void play(View view) {
        Intent openLoading = new Intent(MainMenuActivity.this, LoadingActivity.class);
        startActivity(openLoading);
    }

    public void rules(View view) {
        Intent openRules = new Intent(MainMenuActivity.this, RulesActivity.class);
        startActivity(openRules);
    }

    public void profile(View view) {
        Intent openActivity;
        if (anonymousUser) {
            // open register for anonymous users
            openActivity = new Intent(MainMenuActivity.this, RegisterActivity.class);
        } else {
            // open profile for already logged in users
            openActivity = new Intent(MainMenuActivity.this, ProfileActivity.class);
        }
        startActivity(openActivity);
    }

    public void statistics(View view) {
        Intent openStatistics =
                new Intent(MainMenuActivity.this, StatisticsActivity.class);
        startActivity(openStatistics);
    }

    public void signOut(View view) {
        if (anonymousUser) {
            final FirebaseUser anonUser = FirebaseDB.mAuth.getCurrentUser();
            final String uid = anonUser.getUid();
            FirebaseDB.mDatabase.child("users").child(uid).
                    removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    anonUser.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("ANONYMOUS", "User account deleted.");
                                        finish();
                                    }
                                }
                            });
                }
            });
        }
        else {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing on back pressed
    }
}
