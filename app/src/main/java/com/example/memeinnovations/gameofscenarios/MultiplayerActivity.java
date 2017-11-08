package com.example.memeinnovations.gameofscenarios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;


import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;



public class MultiplayerActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    //Creates the listener attributes for roomConfig
    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        RoomConfig.Builder builder = RoomConfig.builder((RoomUpdateListener) this);
        builder.setMessageReceivedListener((RealTimeMessageReceivedListener) this);
        builder.setRoomStatusUpdateListener((RoomStatusUpdateListener) this);

        return builder;
    }

    //Creates the room for 2 players to duke it out like MEN
    private void startQuickGame() {

        //1v1
        Bundle am = RoomConfig.createAutoMatchCriteria(1,1,0);

        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();

        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    boolean currentlyPlaying = false;

    final static int NEEDED_PLAYERS = 2;
    //final static int MAX_PLAYERS = 2;

    //Will set to true when 2 players enter room
    boolean gameRoomStart(Room room) {

        int connectedPlayers = 0;

        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) ++connectedPlayers;
        }

        return connectedPlayers == NEEDED_PLAYERS;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



}
