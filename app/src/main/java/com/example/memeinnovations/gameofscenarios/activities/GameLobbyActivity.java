package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;

import java.util.Random;


/**
 * Created by Benson Gao on 11/5/2017.
 */

public class GameLobbyActivity extends AppCompatActivity{
    private String gameName;
    private int rulesLayout;
    private TextView title;

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

        // title = (TextView)findViewById(R.id.txtGameTitle);

        // initialize multiplayer class integration VT
        multiplayerSession =
                (Multiplayer) getIntent().getSerializableExtra("multiplayerSession");

        //determine which game is being played
        gameName = multiplayerSession.getChosenScenario();
        updateActivity();
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
                // title.setText(gameName);
                break;

            case "Game of Chicken":
                rulesLayout = R.layout.activity_chicken_rules;
                // title.setText(gameName);
                break;

            case "Traveler's Dilemma":
                rulesLayout = R.layout.activity_travelers_rules;
                // title.setText(gameName);
                break;
        }
    }

    public void reroll(View view){
        Toast.makeText(getApplicationContext(),
                "Re-roll not implemented for multiplayer yet! Sorry.",
                Toast.LENGTH_SHORT).show();

/*        String currentGame = gameName;
        while(currentGame.equals(gameName)){
            chooseGame();
        }
        updateActivity();*/
    }

    public void ready(View view){
        Intent ready;
        switch(gameName){
            case "Prisoner's Dilemma":
                ready = new Intent(GameLobbyActivity.this, PrisonersActivity.class);
                ready.putExtra("multiplayerSession", multiplayerSession);
                startActivity(ready);
                finish();
                break;

            case "Game of Chicken":
                ready = new Intent(GameLobbyActivity.this, ChickenActivity.class);
                ready.putExtra("multiplayerSession", multiplayerSession);
                startActivity(ready);
                finish();
                break;

            case "Traveler's Dilemma":
                ready = new Intent(GameLobbyActivity.this, TravelersActivity.class);
                ready.putExtra("multiplayerSession", multiplayerSession);
                startActivity(ready);
                finish();
                break;
        }
    }
}
