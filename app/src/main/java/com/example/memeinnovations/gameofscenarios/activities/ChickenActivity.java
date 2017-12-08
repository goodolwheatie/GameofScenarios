package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;


public class ChickenActivity extends AppCompatActivity {
    private static int TIME_OUT = 7000; //Time to launch the another activity
    private TextView timer;
    private CountDownTimer gTimer;
    private View activity_chicken;
    private String swerveChoice;
    private Multiplayer multiplayerSession;
    private final TaskCompletionSource<Void> waitSource = new TaskCompletionSource<>();
    private Task waitTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken);

        // grab multiplayer session from game lobby
        multiplayerSession =
                (Multiplayer) getIntent().getSerializableExtra("multiplayerSession");
        waitTask = waitSource.getTask();

        try {
            //disables the actionBar
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        activity_chicken = (ConstraintLayout) findViewById(R.id.activity_chicken);
        timer = (TextView) findViewById(R.id.chickenTimer);
        //creates and starts the timer for the game
        gTimer = new CountDownTimer(TIME_OUT, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                seconds--;
                timer.setText(String.format("%02d", seconds));
                if (seconds < 6) {
                    timer.setTextColor(Color.RED);
                }
            }

            public void onFinish() {
                lockIn(activity_chicken);
            }
        };
        gameTimer();
    }

    public void gameTimer() {
        gTimer.start();
    }

    public void swerveLeft(View view) {
        swerveChoice = "Left";
        lockIn(activity_chicken);
    }

    public void swerveRight(View view) {
        swerveChoice = "Right";
        lockIn(activity_chicken);
    }

    public void stayCenter(View view) {
        swerveChoice = "Center";
        lockIn(activity_chicken);
    }

    public void lockIn(View view) {
        multiplayerSession.makeChoice(swerveChoice);
        multiplayerSession.lockChoice();
        // make sure opponent makes decision before moving on.
        multiplayerSession.checkOtherPlayersChoiceLocked(waitSource);

        final Intent lockIn = new Intent(ChickenActivity.this, GameFinishActivity.class);
        lockIn.putExtra("gameName", "chicken"); //send the game name
        lockIn.putExtra("swerveChoice", swerveChoice); //send the decision made to the next activity

        // wait until other player locks in their choice
        Task<Void> allTask;
        allTask = Tasks.whenAll(waitTask);
        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (gTimer != null) {
                    //close the timer
                    gTimer.cancel();
                }
                lockIn.putExtra("multiplayerSession", multiplayerSession);
                startActivity(lockIn);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        //disables the back button in-game
    }
}
