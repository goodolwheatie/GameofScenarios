package com.example.memeinnovations.gameofscenarios.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.memeinnovations.gameofscenarios.R;
import com.example.memeinnovations.gameofscenarios.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserScenarioProfileFragment extends Fragment {


    private TextView tvwWins, tvwLosses, tvwTotalGames, tvwRewardPoints, tvwDraws;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public UserScenarioProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_scenario_profile, container, false);

        // initialize database and authentication
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users")
                .child(mAuth.getInstance().getCurrentUser().getUid());

        // initialize TextView
        tvwRewardPoints = (TextView) v.findViewById(R.id.tvwCurrRewardPoints);
        tvwWins = (TextView) v.findViewById(R.id.tvwCurrWins);
        tvwDraws = (TextView) v.findViewById(R.id.tvwCurrDraws);
        tvwLosses = (TextView) v.findViewById(R.id.tvwCurrLosses);
        tvwTotalGames = (TextView) v.findViewById(R.id.tvwCurrTotalGames);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                tvwRewardPoints.setText(String.valueOf(currentUser.getRewardPoints()));
                tvwWins.setText(String.valueOf(currentUser.getWins()));
                tvwDraws.setText(String.valueOf(currentUser.getDraws()));
                tvwLosses.setText(String.valueOf(currentUser.getLosses()));
                tvwTotalGames.setText(String.valueOf(currentUser.getTotalGamesPlayed()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        return v;
    }
}
