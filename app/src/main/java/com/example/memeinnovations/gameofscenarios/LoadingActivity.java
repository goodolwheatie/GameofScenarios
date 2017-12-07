package com.example.memeinnovations.gameofscenarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class LoadingActivity extends AppCompatActivity {

    final Multiplayer multiplayerSession = new Multiplayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
