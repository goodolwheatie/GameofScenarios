package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    private static int TIME_OUT = 15000; //Time to launch the another activity
    private boolean betrayed = false;
    final View activity_game = findViewById(R.id.activity_game);
    private TextView timer = (TextView)findViewById(R.id.gameTimer);
    private CountDownTimer gTimer = new CountDownTimer(TIME_OUT* 1000+1000, 1000) {

        public void onTick(long millisUntilFinished) {
            int seconds = (int) (millisUntilFinished / 1000);
            timer.setText(String.format("%02d", seconds));

            if(millisUntilFinished < 5000){
                timer.setTextColor(Color.RED);
            }
        }

        public void onFinish() {
            lockIn(activity_game);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameTimer();
    }

    public void gameTimer(){
        gTimer.start();
    }

    public void betray(View view){
        betrayed = true;
    }

    public void keepQuiet(View view){
        betrayed = false;
    }

    public void lockIn(View view){
        Intent lockIn = new Intent(GameActivity.this, GameFinishActivity.class);
        lockIn.putExtra("Betrayal",betrayed);
        startActivity(lockIn);
        if(gTimer!=null){
            gTimer.cancel();
        }
        finish();
    }

}
