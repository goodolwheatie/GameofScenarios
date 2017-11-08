package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Chronometer;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    private static int TIME_OUT = 15000; //Time to launch the another activity
    private boolean betrayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final View activity_game = findViewById(R.id.activity_game);

        //gameTimer();
    }

/*    public void gameTimer(){

        new CountDownTimer(TIME_OUT, 1000){
            public void onTick(long millisUntilFinished){
                R.id.gameTimer.setText(millisUntilFinished / 1000);
            }

            public void onFinish(){
                lockIn(activity_game);
            }
        }.start();
    }*/

    public void betray(View view){
        betrayed = true;
    }

    public void keepQuiet(View view){
        betrayed = false;
    }

    public void lockIn(View view){
        Intent lockIn = new Intent(GameActivity.this, GameFinishActivity.class);
        startActivity(lockIn);
        finish();
    }

}
