package com.example.memeinnovations.gameofscenarios;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent xD on 10/11/2017.
 */

public class FirebaseDB {

    public static FirebaseAuth getAuthConnection() {
        return FirebaseAuth.getInstance();
    }
    public static DatabaseReference getConnection(){
        return FirebaseDatabase.getInstance().getReference();
    }

}
