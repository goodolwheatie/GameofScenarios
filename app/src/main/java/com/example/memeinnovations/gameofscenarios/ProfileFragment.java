package com.example.memeinnovations.gameofscenarios;


import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private Spinner spnGender, spnAge, spnEthnicity;
    private EditText etUser, etPass, etNewPass;
    private Button btnUpdateClick;
    AuthCredential credential;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                updateProfile();
                break;
        }
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    private void findSpinnerEntry(Spinner spn, String compareValue, @ArrayRes int entries) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getContext(), entries, android.R.layout.simple_spinner_item);
        spn.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spn.setSelection(spinnerPosition);
        }
    }

    private void updateProfile() {
        String currGen = spnGender.getSelectedItem().toString();
        String currAge = spnAge.getSelectedItem().toString();
        String currEthn = spnEthnicity.getSelectedItem().toString();
        String username = etUser.getText().toString().trim();
        final String newPass = etNewPass.getText().toString().trim();

        if (!(newPass.equals("")))
        {
            if (newPass.length() < 6) {
                etNewPass.setError("Minimum length of password should be 6");
                etNewPass.requestFocus();
                return;
            }
            // get user authentication
            String Email = mUser.getEmail();
            String oldPass = etPass.toString();
            credential = EmailAuthProvider.getCredential(mUser.getEmail(), etPass.getText().toString());

            // prompt user to re-authenticate
            mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (username.isEmpty()) {
            etUser.setError("Username is required");
            etUser.requestFocus();
            return;
        }
        if (currGen.equals("Select Option..")) {
            currGen = "";
        }
        if (currAge.equals("Select Option..")) {
            currAge = "";
        }
        if (currEthn.equals("Select Option..")) {
            currEthn = "";
        }

        mDatabase.child("username").setValue(username);
        mDatabase.child("gender").setValue(currGen);
        mDatabase.child("age").setValue(currAge);
        mDatabase.child("ethnicity").setValue(currEthn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // initialize database and authentication
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users")
                .child(mAuth.getInstance().getCurrentUser().getUid());
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        // initialize Spinner
        spnGender = (Spinner) v.findViewById(R.id.spnCurrentGender);
        spnAge = (Spinner) v.findViewById(R.id.spnCurrentAge);
        spnEthnicity = (Spinner) v.findViewById(R.id.spnCurrentEthnicity);

        // initialize EditText
        etUser = (EditText) v.findViewById(R.id.etCurrentUser);
        etPass = (EditText) v.findViewById(R.id.etCurrentPass);
        etNewPass = (EditText) v.findViewById(R.id.etChangePass);

        // initialize button
        btnUpdateClick = v.findViewById(R.id.btnUpdate);
        btnUpdateClick.setOnClickListener(this);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // retrieve user from database
                User currentUser = dataSnapshot.getValue(User.class);

                // initialize spinners to user profile
                findSpinnerEntry(spnGender, currentUser.getGender(), R.array.gender);
                findSpinnerEntry(spnAge, currentUser.getAge(), R.array.age);
                findSpinnerEntry(spnEthnicity, currentUser.getEthnicity(), R.array.ethnicity);

                // set to user's username
                etUser.setText(currentUser.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        return v;
    }

}
