package com.example.memeinnovations.gameofscenarios;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent xD on 11/11/2017.
 * Used if the application is required to do something before actually
 * opening up.
 */

public class MApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        MApplication.context = getApplicationContext();

        // allows the user data to be cached on user device for faster loading.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // keeps data synced on disk for multiplayer
        FirebaseDatabase.getInstance().getReference().child("Prisoner's Dilemma").keepSynced(true);
        FirebaseDatabase.getInstance().getReference().child("Game of Chicken").keepSynced(true);
    }

    public static Context getAppContext() {
        return MApplication.context;
    }
}
