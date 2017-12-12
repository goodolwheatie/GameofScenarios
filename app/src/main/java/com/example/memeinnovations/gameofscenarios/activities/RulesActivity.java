package com.example.memeinnovations.gameofscenarios.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.memeinnovations.gameofscenarios.R;



public class RulesActivity extends AppCompatActivity {
    private ScrollView rulesLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getString(R.string.rules_and_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_rules);
        rulesLayout = (ScrollView) findViewById(R.id.activity_rules);
    }

    public void chickenRules(View v) {
        CardView cardView = findViewById(R.id.cardViewChicken);
        TextView rules = findViewById(R.id.cardViewChickenRules);

        if(rules.getVisibility() == View.GONE){
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.GONE);
        }
    }

    public void prisonersRules(View v) {
        CardView cardView = findViewById(R.id.cardViewPrisoners);
        TextView rules = findViewById(R.id.cardViewPrisonersRules);

        if(rules.getVisibility() == View.GONE){
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.GONE);
        }
    }

    public void travelersRules(View v) {
        CardView cardView = findViewById(R.id.cardViewTravelers);
        TextView rules = findViewById(R.id.cardViewTravelersRules);

        if(rules.getVisibility() == View.GONE){
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.GONE);
        }
    }

    public void aboutGameTheory(View v) {
        CardView cardView = findViewById(R.id.cardViewInfo);
        TextView rules = findViewById(R.id.cardViewInfoContent);

        if(rules.getVisibility() == View.GONE){
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.GONE);
        }
    }

    public void aboutNash(View v) {
        CardView cardView = findViewById(R.id.cardViewNash);
        TextView rules = findViewById(R.id.cardViewNashContent);

        if(rules.getVisibility() == View.GONE){
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.GONE);
        }
    }

    public void aboutPareto(View v) {
        CardView cardView = findViewById(R.id.cardViewPareto);
        TextView rules = findViewById(R.id.cardViewParetoContent);


        if(rules.getVisibility() == View.GONE){
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(cardView);
            rules.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
