package com.example.memeinnovations.gameofscenarios;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent xD on 10/11/2017.
 * So I don't have to retype the same code over and over.
 *
 */

public class FirebaseDB {
    public final static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public final static DatabaseReference mDatabase =
            FirebaseDatabase.getInstance().getReference();
    public static FirebaseAuth getAuthConnection() {
        return FirebaseAuth.getInstance();
    }
    public static DatabaseReference getConnection(){
        return FirebaseDatabase.getInstance().getReference();
    }

}
