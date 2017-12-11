package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;

public class GameFinishActivity extends AppCompatActivity {

    private static double PROBABILITY_OF_BETRAY = 60;
    private static double PROBABILITY_OF_LEFT = 30;
    private static double PROBABILITY_OF_CENTER = 70;
    private String gameName = "";
    private Bundle bundles;
    private TextView playerChoice;
    private TextView opponentChoice;
    private TextView rewards;
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

        playerChoice = (TextView) findViewById(R.id.txtPlayerChoice);
        opponentChoice = (TextView) findViewById(R.id.txtOpponentChoice);
        rewards = (TextView) findViewById(R.id.txtRewards);

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

        if(betrayed){
            playerChoice.setText(String.format(getString(R.string.yourChoice), getString(R.string.betray)));
        }else{
            playerChoice.setText(String.format(getString(R.string.yourChoice), getString(R.string.keepQuiet)));
        }

        if(otherBetrayed){
            opponentChoice.setText(String.format(getString(R.string.opponentChoice), getString(R.string.betray)));
        }else{
            opponentChoice.setText(String.format(getString(R.string.opponentChoice), getString(R.string.keepQuiet)));
        }

        int points = 0;
        //determine the results of the match
        if (betrayed && otherBetrayed) {
            points = 30;
            multiplayerSession.incrementDraws(points);
        } else {
            if (betrayed) {
                points = 50;
                multiplayerSession.incrementWin(points);
            } else if (otherBetrayed) {
                points = 20;
                multiplayerSession.incrementLoss(points);
            } else {
                points = 40;
                multiplayerSession.incrementDraws(points);
            }
        }
        rewards.setText(String.format(getString(R.string.rewards), points));
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
        playerChoice.setText(String.format(getString(R.string.yourChoice), swerveChoice));
        opponentChoice.setText(String.format(getString(R.string.opponentChoice), opponentChoice));


        int points = 0;
        switch (swerveChoice) { //results logic
            case "Left": {
                switch (otherPlayersChoice) {
                    case "Left":
                        points = 30;
                        multiplayerSession.incrementDraws(points);
                        break;
                    case "Center":
                        points = 30;
                        multiplayerSession.incrementLoss(points);
                        break;
                    case "Right":
                        points = 20;
                        multiplayerSession.incrementLoss(points);
                        break;
                }
                break;
            }
            case "Center": {
                switch (otherPlayersChoice) {
                    case "Left":
                        points = 50;
                        multiplayerSession.incrementWin(points);
                        break;
                    case "Center":
                        points = 20;
                        multiplayerSession.incrementLoss(points);
                        break;
                    case "Right":
                        points = 50;
                        multiplayerSession.incrementWin(points);
                        break;
                }
                break;
            }
            case "Right": {
                switch (otherPlayersChoice) {
                    case "Left":
                        points = 20;
                        multiplayerSession.incrementLoss(points);
                        break;
                    case "Center":
                        points = 30;
                        multiplayerSession.incrementLoss(points);
                        break;
                    case "Right":
                        points = 30;
                        multiplayerSession.incrementDraws(points);
                        break;
                }
                break;
            }
        }
        rewards.setText(String.format(getString(R.string.rewards), points));
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

        playerChoice.setText(String.format(getString(R.string.yourPrice), playerPrice));
        opponentChoice.setText(String.format(getString(R.string.opponentPrice), otherPlayersPrice));


        // changed from compPrice VT
        int difference = otherPlayersPrice - playerPrice;
        int payout = 0;
        if (difference == 0) { //same price
            payout = playerPrice;
            multiplayerSession.incrementDraws((int) (playerPrice/2));
        } else if (difference > 0) { //comp's price was greater than the player's
            payout = (int) (playerPrice + 2 + difference * 0.5 + 0.5);
            multiplayerSession.incrementWin((int) (payout/2));
        } else if (difference < 0) { //comp's price was lower than the player's
            payout = otherPlayersPrice;
            multiplayerSession.incrementLoss((int) (otherPlayersPrice/2));
        }
        rewards.setText(String.format(getString(R.string.rewards), (int) payout/2));
    }

    public void mainMenu(View view) {
        multiplayerSession.incrementTotalGames();
        multiplayerSession.finishGame();
        //return to the main menu
        Intent mainMenu = new Intent(GameFinishActivity.this, MainMenuActivity.class);
        mainMenu.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(mainMenu, 0);
        finish();
    }

    public void playAgain(View view) {
        multiplayerSession.incrementTotalGames();
        multiplayerSession.finishGame();
        //returns to the game lobby page
        Intent playAgain = new Intent(GameFinishActivity.this, LoadingActivity.class);
        startActivity(playAgain);
        finish();
    }
}
