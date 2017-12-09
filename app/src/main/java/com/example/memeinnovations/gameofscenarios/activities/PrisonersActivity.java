package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.view.View;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

public class PrisonersActivity extends AppCompatActivity {
    private static int TIME_OUT = 17000; //Time to launch the another activity
    // private String betrayed = "false";
    private boolean betrayed = false;
    private View activity_prisoners;
    private TextView timerText;
    private CountDownTimer gTimer;
    private ToggleButton btnBetray;
    private ToggleButton btnKeepQuiet;
    private Multiplayer multiplayerSession;
    // initialize wait source task.
    private final TaskCompletionSource<Void> waitSource = new TaskCompletionSource<>();
    private Task waitTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisoners);

        // grab multiplayer session from game lobby
        multiplayerSession =
                (Multiplayer) getIntent().getSerializableExtra("multiplayerSession");
        waitTask = waitSource.getTask();


        try
        {
            //disables the actionBar
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        activity_prisoners = (ConstraintLayout) findViewById(R.id.activity_prisoners);
        timerText = (TextView) findViewById(R.id.prisonersTimer);
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
            public void onFinish() {
                lockIn(activity_prisoners);
            }
        };
        gameTimer();

        //have the game initialize with the keeping quiet option
        btnBetray = (ToggleButton) findViewById(R.id.btnBetray);
        btnKeepQuiet = (ToggleButton) findViewById(R.id.btnKeepQuiet);
        keepQuiet(findViewById(R.id.activity_prisoners));
    }

    public void gameTimer() {
        gTimer.start();
    }

    public void betray(View view) {
        // VT changes
        betrayed = true;
        // set choice in DB
        multiplayerSession.makeChoice("true");

        // betrayed = true;
        //check this button
        btnBetray.setChecked(true);
        //uncheck other button
        btnKeepQuiet.setChecked(false);
    }

    public void keepQuiet(View view) {
        // VT changes
        betrayed = false;
        // set choice in DB
        multiplayerSession.makeChoice("false");

        // betrayed = false;
        //check this button
        btnKeepQuiet.setChecked(true);
        //uncheck other button
        btnBetray.setChecked(false);
    }

    public void lockIn(View view) {
        // lock in DB
        multiplayerSession.lockChoice();

        // make sure opponent makes decision before moving on.
        multiplayerSession.checkOtherPlayersChoiceLocked(waitSource);

        final Intent lockIn =
                new Intent(PrisonersActivity.this, GameFinishActivity.class);
        lockIn.putExtra("gameName", "prisoners"); //send the game name
        lockIn.putExtra("Betrayal", betrayed); //send the decision made to the next activity

        // check if all task are finished stall until other player has locked in their choice
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
