package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void initReg() {
        Button btnReg = (Button) findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(openRegister);
            }
        });
    }
    public void initAnon() {
        Button btnAnon = (Button) findViewById(R.id.btnAnonymously);
        btnAnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(openMainMenu);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize button listeners
        initReg();
        initAnon();
    }
}
