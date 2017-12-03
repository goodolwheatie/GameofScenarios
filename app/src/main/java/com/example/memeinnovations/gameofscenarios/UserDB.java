package com.example.memeinnovations.gameofscenarios;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vincent xD on 10/11/2017.
 */

public class UserDB {

    public static DatabaseReference getConnection(){
        return FirebaseDatabase.getInstance().getReference();
    }


}
