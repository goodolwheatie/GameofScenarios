package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.Random;


/**
 * Created by Benson Gao on 11/5/2017.
 */

public class GameLobbyActivity extends AppCompatActivity{
    private String gameName;
    private int rulesLayout;
    private ImageView ImageViewer;
    private ImageView theOtherImageView;
    private Button btnReroll;
    private Button btnReady;

    // Multiplayer class for multiplayer implementation.
    private Multiplayer multiplayerSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            //disables the actionBar
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_game_lobby);
        btnReroll = (Button) findViewById(R.id.btnReroll);
        btnReady = (Button) findViewById(R.id.btnReady);
        ImageViewer = (ImageView) findViewById(R.id.imageViewLobby2);
        theOtherImageView = (ImageView) findViewById(R.id.imageViewLobby);

        // initialize multiplayer class integration VT
        multiplayerSession =
                (Multiplayer) getIntent().getSerializableExtra("multiplayerSession");

        //determine which game is being played
        gameName = multiplayerSession.getChosenScenario();
        updateActivity();

        final TaskCompletionSource<Void> waitSource = new TaskCompletionSource<>();
        final Task waitTask = waitSource.getTask();
        final TaskCompletionSource<Void> delaySource = new TaskCompletionSource<>();
        final Task delayTask = delaySource.getTask();

        // begin a reroll phase
        multiplayerSession.checkRerolled(waitSource);
        btnReady.setEnabled(false);
        btnReady.setText(R.string.reroll_phase);
        Toast.makeText(getApplicationContext(), "Begin Re-roll phase....",
                Toast.LENGTH_SHORT).show();

        // set delay for reroll phase.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Re-roll phase has ended.",
                        Toast.LENGTH_SHORT).show();
                waitSource.trySetResult(null);
                btnReady.setText(R.string.ready);
                btnReady.setEnabled(true);
                btnReroll.setEnabled(false);
            }
        }, 10000);

        Tasks.whenAll(waitTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                gameName = multiplayerSession.getChosenScenario();
                updateActivity();

             /*   // incase other player re-rolls after a re-roll.
                multiplayerSession.checkRerolled(delaySource);
                Tasks.whenAll(delayTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        gameName = multiplayerSession.getChosenScenario();
                        updateActivity();
                    }
                });*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        //disables the back button
    }

    public void popupRules(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(rulesLayout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupRules = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupRules.showAtLocation(findViewById(R.id.activity_game_lobby).getRootView(), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupRules.dismiss();
                return true;            }
        });
    }

    public void chooseGame(){
        Random r = new Random();
        int gameInt = r.nextInt(2);
        switch(gameInt) {
            case 0:{
                gameName = "Prisoner's Dilemma";
                break;
            }
            case 1:{
                gameName = "Game of Chicken";
                break;
            }
        }
    }

    public void updateActivity(){
        switch(gameName){
            case "Prisoner's Dilemma":
                rulesLayout = R.layout.activity_prisoners_rules;
                theOtherImageView.setImageResource(R.drawable.pdilemmatitle);
                ImageViewer.setImageResource(R.drawable.prisdil);
                break;

            case "Game of Chicken":
                rulesLayout = R.layout.activity_chicken_rules;
                theOtherImageView.setImageResource(R.drawable.chickentitle);
                ImageViewer.setImageResource(R.drawable.swerve);
                break;

            case "Traveler's Dilemma":
                rulesLayout = R.layout.activity_travelers_rules;
                theOtherImageView.setImageResource(R.drawable.travelertitle);
                ImageViewer.setImageResource(R.drawable.moneyineyes);
                break;
        }
    }

    public void reroll(View view){
        // wait tasks for loading DB data.
        final TaskCompletionSource<Void> waitSource = new TaskCompletionSource<>();
        Task waitTask = waitSource.getTask();

        // only one reroll is allowed.
        btnReroll.setEnabled(false);
        btnReroll.setBackgroundTintList
                (ColorStateList.valueOf(getResources().getColor(R.color.colorButtonPressed)));

        multiplayerSession.rerollScenario(waitSource);
        Toast.makeText(getApplicationContext(), "Rerolling Scenario...",
                Toast.LENGTH_SHORT).show();
        Tasks.whenAll(waitTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                gameName = multiplayerSession.getChosenScenario();
                updateActivity();
            }
        });
    }

    public void ready(View view){
        btnReady.setEnabled(false);
        multiplayerSession.setReady();

        // check if other person is ready....
        final TaskCompletionSource<Void> waitSource = new TaskCompletionSource<>();
        final Task waitTask = waitSource.getTask();
        multiplayerSession.checkReady(waitSource);
        Tasks.whenAll(waitTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent ready;
                switch(gameName){
                    case "Prisoner's Dilemma":
                        ready = new Intent
                                (GameLobbyActivity.this, PrisonersActivity.class);
                        ready.putExtra("multiplayerSession", multiplayerSession);
                        startActivity(ready);
                        finish();
                        break;

                    case "Game of Chicken":
                        ready = new Intent
                                (GameLobbyActivity.this, ChickenActivity.class);
                        ready.putExtra("multiplayerSession", multiplayerSession);
                        startActivity(ready);
                        finish();
                        break;

                    case "Traveler's Dilemma":
                        ready = new Intent
                                (GameLobbyActivity.this, TravelersActivity.class);
                        ready.putExtra("multiplayerSession", multiplayerSession);
                        startActivity(ready);
                        finish();
                        break;
                }
            }
        });
    }
}
