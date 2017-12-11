package com.example.memeinnovations.gameofscenarios.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;



public class RulesActivity extends AppCompatActivity {
    private int rulesLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getString(R.string.rules));

        setContentView(R.layout.activity_rules);

    }


    public void prisonerRules(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_prisoners_rules, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow prisonerRules = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        prisonerRules.showAtLocation(findViewById(R.id.activity_rules).getRootView(), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                prisonerRules.dismiss();
                return true;            }
        });
    }

    public void chickenRules(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_chicken_rules, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow chickenRules = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        chickenRules.showAtLocation(findViewById(R.id.activity_rules).getRootView(), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                chickenRules.dismiss();
                return true;            }
        });
    }

    public void travelerRules(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_travelers_rules, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow travelerRules = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        travelerRules.showAtLocation(findViewById(R.id.activity_rules).getRootView(), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                travelerRules.dismiss();
                return true;            }
        });
    }



}
