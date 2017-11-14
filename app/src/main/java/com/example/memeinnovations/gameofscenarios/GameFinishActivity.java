package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Game;

import java.util.Random;

public class GameFinishActivity extends AppCompatActivity {

    private static double PROBABILITY_OF_BETRAY = 60; //Time to launch the another activity
    private boolean betrayed = false;
    private boolean compBetrayed = false;
    TextView choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);

        try
        {
            //disables the actionBar
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        choice = (TextView)findViewById(R.id.txtChoice);
        Bundle bundles= getIntent().getExtras();
        betrayed = bundles.getBoolean("Betrayal");
        compBetrayed = singlePlayerCalcs();

        //determine the results of the match
        if(betrayed && compBetrayed) {
            choice.setText("You both chose to betray your partner");
        }else{
            if(betrayed){
                choice.setText("You chose to betray your partner but your partner chose to keep quiet.");
            }
            if(compBetrayed){
                choice.setText("You chose to keep quiet but your partner chose to betray you.");
            }
            if(!betrayed && !compBetrayed){
                choice.setText("You both chose to keep quiet.");
            }
        }
    }

    public boolean singlePlayerCalcs(){
        //generate a random decision for the computer to make
        Random r = new Random();
        int compChoiceInt = r.nextInt(100);
        if(compChoiceInt > PROBABILITY_OF_BETRAY){
            //computer chooses to keep quiet
            return false;
        }else{
            //computer chooses to betray
            return true;
        }
    }

    public void mainMenu(View view){
        //return to the main menu
        Intent mainMenu = new Intent(GameFinishActivity.this, MainMenuActivity.class);
        startActivity(mainMenu);
        finish();
    }

    public void playAgain(View view){
        //returns to the game lobby page
        Intent playAgain = new Intent(GameFinishActivity.this, GameLobbyActivity.class);
        startActivity(playAgain);
        finish();
    }
}
