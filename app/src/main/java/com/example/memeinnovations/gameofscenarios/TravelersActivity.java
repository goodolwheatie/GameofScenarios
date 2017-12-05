package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.NumberPicker;
import android.view.View;

public class TravelersActivity extends AppCompatActivity {
    private static int TIME_OUT = 17000; //Time to launch the next activity
    private View activity_travelers;
    private TextView timerText;
    private CountDownTimer gTimer;
    private NumberPicker picker;
    private int price = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelers);

        try
        {
            //disables the actionBar
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        activity_travelers = (ConstraintLayout) findViewById(R.id.activity_travelers);
        timerText = (TextView) findViewById(R.id.travelersTimer);
        //creates and starts the timer for the game
        gTimer = new CountDownTimer(TIME_OUT, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                seconds--;
                timerText.setText(String.format("%02d", seconds));
                if (seconds < 6) {
                    timerText.setTextColor(Color.RED);
                }
            }
            public void onFinish() {  lockIn(activity_travelers); }
        };
        gameTimer();

        //create and initialize the number picker
        numPicker();
    }

    public void gameTimer() { gTimer.start(); }

    public void numPicker(){
        String [] dollarValues = new String [99];

        for(int i = 0; i < 99; i++){
            dollarValues[i] = "$" + String.valueOf(i+2);
        }

        picker = (NumberPicker) findViewById(R.id.numberPickerTraveler);
        picker.setMinValue(2);
        picker.setMaxValue(100);
        picker.setDisplayedValues(dollarValues);
        picker.setValue(51);
    }

    public void lockIn(View view){
        Intent lockIn = new Intent( TravelersActivity.this, GameFinishActivity.class);
        lockIn.putExtra( "gameName", "travelers"); //send the game name

        price = picker.getValue();
        lockIn.putExtra( "price", price); //send the price chosen to the next activity
        startActivity(lockIn);
        if(gTimer != null){
            //close the timer
            gTimer.cancel();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        //disables the back button in-game
    }
}
