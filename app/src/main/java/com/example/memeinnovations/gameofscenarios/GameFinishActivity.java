package com.example.memeinnovations.gameofscenarios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameFinishActivity extends AppCompatActivity {

    private boolean betrayed = false;
    TextView choice = (TextView)findViewById(R.id.txtChoice);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);

        Bundle bundles= getIntent().getExtras();
        betrayed = bundles.getBoolean("Betrayal");
        if(betrayed){
            choice.setText("You betrayed your partner");
        }else{
            choice.setText("You kept quiet");
        }
    }
}
