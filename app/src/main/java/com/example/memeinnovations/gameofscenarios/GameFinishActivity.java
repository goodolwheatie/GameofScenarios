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
    private Multiplayer multiplayerSession;

    // store other players choice from DB
    private String otherPlayersChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);

        // get multiplayer session
        multiplayerSession =
                (Multiplayer) getIntent().getSerializableExtra("multiplayerSession");
        otherPlayersChoice = multiplayerSession.getOtherPlayersChoice();

        try {
            //disables the actionBar
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        results = (TextView) findViewById(R.id.txtResults);
        bundles = getIntent().getExtras();

        if (bundles != null) {
            gameName = bundles.getString("gameName");
        }

        switch (gameName) {
            case "prisoners":
                prisoners();
                break;

            case "chicken":
                chicken();
                break;

            case "travelers":
                travelers();
                break;
        }
    }

    public void prisoners() {
/*        boolean compBetrayed;
        //generate a random decision for the computer to make
        Random r = new Random();
        int compChoiceInt = r.nextInt(100);
        if(compChoiceInt > PROBABILITY_OF_BETRAY){
            //computer chooses to keep quiet
            compBetrayed = false;
        }else{
            //computer chooses to betray
            compBetrayed = true;
        }*/

        boolean otherBetrayed;
        if (otherPlayersChoice.equals("true")) {
            otherBetrayed = true;
        } else {
            otherBetrayed = false;
        }

        boolean betrayed = false;
        //retrieve choice from previous activity
        if (bundles != null) {
            betrayed = bundles.getBoolean("Betrayal");
        }


        //determine the results of the match
        if (betrayed && otherBetrayed) {
            results.setText("You both chose to betray your partner");
        } else {
            if (betrayed) {
                results.setText("You chose to betray your partner but your partner chose to keep quiet.");
                multiplayerSession.incrementWin();
            } else if (otherBetrayed) {
                results.setText("You chose to keep quiet but your partner chose to betray you.");
                multiplayerSession.incrementLoss();
            } else if (!betrayed && !otherBetrayed) {
                results.setText("You both chose to keep quiet.");
            }
        }
        multiplayerSession.finishGame();

    }

    public void chicken() {
/*        String compSwerve;
        //generate a random decision for the computer to make
        Random r = new Random();
        int compChoiceInt = r.nextInt(100);
        if(compChoiceInt < PROBABILITY_OF_LEFT){
            compSwerve = "Left";
        }else if(compChoiceInt < PROBABILITY_OF_CENTER){
            compSwerve = "Center";
        }else{
            compSwerve = "Right";
        }*/

        //retrieve your choice from previous activity
        String swerveChoice = bundles.getString("swerveChoice");

        switch (swerveChoice) { //results logic
            case "Left": {
                switch (otherPlayersChoice) {
                    case "Left":
                        results.setText("You both swerved but didn't hit each other");
                        multiplayerSession.incrementLoss();
                        break;
                    case "Center":
                        results.setText("You swerved but your opponent stayed the course");
                        multiplayerSession.incrementLoss();
                        break;
                    case "Right":
                        results.setText("You swerved into each other and crashed");
                        multiplayerSession.incrementLoss();
                        break;
                }
                break;
            }
            case "Center": {
                switch (otherPlayersChoice) {
                    case "Left":
                        results.setText("You stayed the course and your opponent had to swerve");
                        multiplayerSession.incrementWin();
                        break;
                    case "Center":
                        results.setText("You both stayed the course and crashed");
                        multiplayerSession.incrementLoss();
                        break;
                    case "Right":
                        results.setText("You stayed the course and your opponent had to swerve");
                        multiplayerSession.incrementWin();
                        break;
                }
                break;
            }
            case "Right": {
                switch (otherPlayersChoice) {
                    case "Left":
                        results.setText("You swerved into each other and crashed");
                        multiplayerSession.incrementLoss();
                        break;
                    case "Center":
                        results.setText("You swerved but your opponent stayed the course");
                        multiplayerSession.incrementLoss();
                        break;
                    case "Right":
                        results.setText("You both swerved but didn't hit each other");
                        multiplayerSession.incrementLoss();
                        break;
                }
                break;
            }
        }
        multiplayerSession.finishGame();
    }

    public void travelers() {

        int otherPlayersPrice = Integer.parseInt(otherPlayersChoice);
/*
        //generate a random price for the computer to choose
        Random r = new Random();
        int compPrice = r.nextInt(99) + 2;
*/

        //retrieve your choice from previous activity
        int playerPrice = bundles.getInt("price");

        // changed from compPrice VT
        int difference = otherPlayersPrice - playerPrice;
        if (difference == 0) { //same price
            results.setText("You both agreed on the price at $" + playerPrice + "You got paid $" + playerPrice);
        } else if (difference > 0) { //comp's price was greater than the player's
            int payout = (int) (playerPrice + 2 + difference * 0.5 + 0.5);
            results.setText("You gave a price of $" + playerPrice +
                    " while your oppponent gave a price of $" +
                    otherPlayersPrice + ". You got paid $" + payout);
            multiplayerSession.incrementWin();
        } else if (difference < 0) { //comp's price was lower than the player's
            results.setText("You gave a price of $" + playerPrice +
                    " while your oppponent gave a price of $" + otherPlayersPrice +
                    ". You got paid $" + otherPlayersPrice);
            multiplayerSession.incrementLoss();
        }
        multiplayerSession.finishGame();
    }

    public void mainMenu(View view) {
        //return to the main menu
        Intent mainMenu = new Intent(GameFinishActivity.this, MainMenuActivity.class);
        mainMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(mainMenu, 0);
        finish();
    }

    public void playAgain(View view) {
        //returns to the game lobby page
        Intent playAgain = new Intent(GameFinishActivity.this, LoadingActivity.class);
        startActivity(playAgain);
        finish();
    }
}
