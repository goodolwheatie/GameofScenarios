package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    private static int TIME_OUT = 15000; //Time to launch the another activity
    private boolean betrayed = false;
    private View activity_game;
    private TextView timer;
    private CountDownTimer gTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        activity_game = (ConstraintLayout) findViewById(R.id.activity_game);
        timer = (TextView) findViewById(R.id.gameTimer);
        gTimer = new CountDownTimer(TIME_OUT, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                timer.setText(String.format("%02d", seconds));

                if (millisUntilFinished < 5000) {
                    timer.setTextColor(Color.RED);
                }
            }

            public void onFinish() {
                lockIn(activity_game);
            }
        };
        gameTimer();
    }

    public void gameTimer() {
        gTimer.start();
    }

    public void betray(View view) {
        betrayed = true;
        //disable this button
        Button btnBetray = (Button) findViewById(R.id.btnBetray);
        btnBetray.setEnabled(false);
        btnBetray.setBackgroundColor(Color.parseColor("#9E9E9E"));
        //enable other button
        Button btnKeepQuiet = (Button) findViewById(R.id.btnKeepQuiet);
        btnKeepQuiet.setEnabled(true);
        btnKeepQuiet.setBackgroundColor(Color.parseColor("#BDBDBD"));
    }

    public void keepQuiet(View view) {
        betrayed = false;
        //disable this button
        Button btnKeepQuiet = (Button) findViewById(R.id.btnKeepQuiet);
        btnKeepQuiet.setEnabled(false);
        btnKeepQuiet.setBackgroundColor(Color.parseColor("#9E9E9E"));
        //enable other button
        Button btnBetray = (Button) findViewById(R.id.btnBetray);
        btnBetray.setEnabled(false);
        btnBetray.setBackgroundColor(Color.parseColor("#BDBDBD"));
    }

    public void lockIn(View view) {
        Intent lockIn = new Intent(GameActivity.this, GameFinishActivity.class);
        lockIn.putExtra("Betrayal", betrayed);
        startActivity(lockIn);
        if (gTimer != null) {
            gTimer.cancel();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
    }

}
