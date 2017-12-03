package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class GameFinishActivity extends AppCompatActivity {

    private static double PROBABILITY_OF_BETRAY = 60;
    private static double PROBABILITY_OF_LEFT = 30;
    private static double PROBABILITY_OF_CENTER = 70;
    private String gameName = "";
    private Bundle bundles;
    private TextView results;

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

        results = (TextView)findViewById(R.id.txtResults);
        bundles= getIntent().getExtras();

        gameName = bundles.getString("gameName");

        switch(gameName){
            case "prisoners":
                prisoners();
                break;

            case "chicken":
                chicken();
                break;
        }
    }

    public void prisoners(){
        boolean compBetrayed;
        //generate a random decision for the computer to make
        Random r = new Random();
        int compChoiceInt = r.nextInt(100);
        if(compChoiceInt > PROBABILITY_OF_BETRAY){
            //computer chooses to keep quiet
            compBetrayed = false;
        }else{
            //computer chooses to betray
            compBetrayed = true;
        }

        //retrieve choice from previous activity
        boolean betrayed = bundles.getBoolean("Betrayal");

        //determine the results of the match
        if(betrayed && compBetrayed) {
            results.setText("You both chose to betray your partner");
        }else{
            if(betrayed){
                results.setText("You chose to betray your partner but your partner chose to keep quiet.");
            }
            if(compBetrayed){
                results.setText("You chose to keep quiet but your partner chose to betray you.");
            }
            if(!betrayed && !compBetrayed){
                results.setText("You both chose to keep quiet.");
            }
        }

    }

    public void chicken(){
        String compSwerve;
        //generate a random decision for the computer to make
        Random r = new Random();
        int compChoiceInt = r.nextInt(100);
        if(compChoiceInt < PROBABILITY_OF_LEFT){
            compSwerve = "Left";
        }else if(compChoiceInt < PROBABILITY_OF_CENTER){
            compSwerve = "Center";
        }else{
            compSwerve = "Right";
        }

        //retrieve your choice from previous activity
        String swerveChoice = bundles.getString("swerveChoice");

        switch(swerveChoice){ //results logic
            case "Left": {
                switch(compSwerve){
                    case "Left":
                        results.setText("You both swerved but didn't hit each other");
                        break;
                    case "Center":
                        results.setText("You swerved but your opponent stayed the course");
                        break;
                    case "Right":
                        results.setText("You swerved into each other and crashed");
                        break;
                }
                break;
            }
            case "Center": {
                switch(compSwerve){
                    case "Left":
                        results.setText("You stayed the course and your opponent had to swerve");
                        break;
                    case "Center":
                        results.setText("You both stayed the course and crashed");
                        break;
                    case "Right":
                        results.setText("You stayed the course and your opponent had to swerve");
                        break;
                }
                break;
            }
            case "Right": {
                switch(compSwerve){
                    case "Left":
                        results.setText("You swerved into each other and crashed");
                        break;
                    case "Center":
                        results.setText("You swerved but your opponent stayed the course");
                        break;
                    case "Right":
                        results.setText("You both swerved but didn't hit each other");
                        break;
                }
                break;
            }
        }
    }

    public void mainMenu(View view){
        //return to the main menu
        Intent mainMenu = new Intent(GameFinishActivity.this, MainMenuActivity.class);
        mainMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(mainMenu,0);
        finish();
    }

    public void playAgain(View view){
        //returns to the game lobby page
        Intent playAgain = new Intent(GameFinishActivity.this, GameLobbyActivity.class);
        startActivity(playAgain);
        finish();
    }
}
