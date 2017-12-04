package com.example.memeinnovations.gameofscenarios;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent xD on 11/11/2017.
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // allows the user data to be cached on user device for faster loading.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().child("Prisoner's Dilemma").keepSynced(true);
        FirebaseDatabase.getInstance().getReference().child("Game of Chicken").keepSynced(true);
    }
}
