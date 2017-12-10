package com.example.memeinnovations.gameofscenarios.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.memeinnovations.gameofscenarios.multiplayer.Multiplayer;
import com.example.memeinnovations.gameofscenarios.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.wang.avi.AVLoadingIndicatorView;

public class LoadingActivity extends AppCompatActivity {

    Multiplayer multiplayerSession;
    private AVLoadingIndicatorView avLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        multiplayerSession = new Multiplayer();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // initialize wait source task.
        final TaskCompletionSource<Void> waitSource = new TaskCompletionSource<>();
        Task waitTask = waitSource.getTask();

        // initialize mutiplayer session
        multiplayerSession.quickPlay(waitSource);

        // check if all task are finished.
        Task<Void> allTask;
        allTask = Tasks.whenAll(waitTask);

        // set loading bar visible
        avLoading = findViewById(R.id.aviLoading);
        avLoading.setVisibility(View.VISIBLE);

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                avLoading.setVisibility(View.GONE);
                Intent intent =
                        new Intent(LoadingActivity.this, GameLobbyActivity.class);
                intent.putExtra("multiplayerSession", multiplayerSession);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        multiplayerSession.onBackPressed();
    }
}
