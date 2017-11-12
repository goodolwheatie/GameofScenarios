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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
