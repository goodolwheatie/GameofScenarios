package com.example.memeinnovations.gameofscenarios;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

/**
 * Created by Vincent on 11/19/2017.
 */

public class Multiplayer extends Activity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // store room database reference for connectplayer() method
    private DatabaseReference connectPlayers;

    // store user information in an object to obtain from the database later.
    private User thisPlayer;

    // stores if current player is player 1 or player 2
    private boolean isPlayer1;

    // sets if a room was found or not
    private boolean foundRoom;
    private RoomStatus currentRoom;

    // sets chosen scenario
    private String chosenScenario;

    // sets chosen room
    private String chosenRoom;

    // iterator for chooseRoom() function
    int roomIterator;

    // store if players are connected
    private boolean arePlayersConnected;

    // set context of application
    private Context context;

    public Multiplayer() {
        currentRoom = new RoomStatus();
        foundRoom = false;
        isPlayer1 = false;
        chosenScenario = "";
        chosenRoom = "";
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arePlayersConnected = false;
    }

    public Multiplayer(Context c) {
        currentRoom = new RoomStatus();
        foundRoom = false;
        isPlayer1 = false;
        chosenScenario = "";
        chosenRoom = "";
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arePlayersConnected = false;
        context = c;
    }

    public User getPlayer() {
        DatabaseReference currentPlayer;
        if (isPlayer1) {
            currentPlayer =
                    mDatabase.child("users").child(currentRoom.player1);
        } else {
            currentPlayer =
                    mDatabase.child("users").child(currentRoom.player2);
        }

        currentPlayer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisPlayer = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return thisPlayer;
    }

    public String chooseScenario() {
        String[] array =
                context.getResources().getStringArray(R.array.scenarios);
        chosenScenario = array[new Random().nextInt(array.length)];
        return chosenScenario;
    }

    private void chooseRoom() {
        final String[] array =
                context.getResources().getStringArray(R.array.rooms);
        final DatabaseReference checkRoom =
                mDatabase.child(chosenScenario);
        checkRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot inUseSnap : dataSnapshot.getChildren()) {
                    // check if room is in-use
                    currentRoom.in_use = (Boolean) inUseSnap.child("in_use").getValue();
                    callBackChooseRoom(array, checkRoom);

                    // iterator through list
                    roomIterator = (roomIterator + 1) % array.length;

                    if (foundRoom) {
                        // connect players using another callback.
                        connectPlayers();
                        // break the foreach loop when a room is found.
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void callBackChooseRoom(String[] roomsArray,
                                    DatabaseReference pCheckRoom) {
        if (!currentRoom.in_use) {
            foundRoom = true;
            chosenRoom = roomsArray[roomIterator];
            pCheckRoom.child(roomsArray[roomIterator]).child("in_use").setValue(true);
        }
    }

    private void connectPlayers() {
        final DatabaseReference connectPlayers =
                mDatabase.child(chosenScenario).child(chosenRoom);

        connectPlayers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentRoom = dataSnapshot.getValue(RoomStatus.class);
                if (!currentRoom.p1connected) {
                    currentRoom.p1connected = true;
                    currentRoom.player1 = mAuth.getCurrentUser().getUid();
                    isPlayer1 = true;
                    connectPlayers.child("p1connected").setValue(true);
                    connectPlayers.child("player1").setValue(mAuth.getCurrentUser().getUid());
                } else {
                    currentRoom.p2connected = true;
                    currentRoom.player2 = mAuth.getCurrentUser().getUid();
                    connectPlayers.child("p2connected").setValue(true);
                    connectPlayers.child("player2").setValue(mAuth.getCurrentUser().getUid());
                }
                ValueEventListener listenForPlayersConnected =
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (isPlayer1) {
                                    currentRoom.player2 =
                                            dataSnapshot.child("player2").getValue(String.class);
                                    currentRoom.p2connected =
                                            (boolean) dataSnapshot.child("p2connected").getValue();
                                } else {
                                    currentRoom.player1 =
                                            dataSnapshot.child("player1").getValue(String.class);
                                    currentRoom.p1connected =
                                            (boolean) dataSnapshot.child("p1connected").getValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                connectPlayers.addValueEventListener(listenForPlayersConnected);
                while (!currentRoom.p1connected && !currentRoom.p2connected);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public String quickPlay() {
        chooseScenario();
        chooseRoom();
        return chosenScenario;
    }

    public void makeChoice(String choice) {
        String currentPlayer;
        if (currentRoom.p1connected) {
            currentRoom.p1choice = choice;
            currentPlayer = "p1choice";
        } else {
            currentRoom.p2choice = choice;
            currentPlayer = "p2choice";
        }
        DatabaseReference thisPlayer =
                mDatabase.child(chosenScenario).child(chosenRoom).child(currentPlayer);
        thisPlayer.setValue(choice);
    }

    public String getOtherPlayersChoice() {
        String playerChoice = "";
        if (isPlayer1) {
            playerChoice = "p2choice";

        } else {
            playerChoice = "p1choice";
        }
        DatabaseReference getDBResults =
                mDatabase.child(chosenScenario).
                        child(chosenRoom).child(playerChoice);

        getDBResults.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isPlayer1) {
                    currentRoom.p2choice = (String) dataSnapshot.getValue();
                } else {
                    currentRoom.p1choice = (String) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        if (isPlayer1) {
            return currentRoom.p2choice;
        }
        return currentRoom.p1choice;
    }

    public boolean isOtherPlayersChoiceLocked() {
        DatabaseReference getDBChoiceLocked;
        if (isPlayer1) {
            getDBChoiceLocked =
                    mDatabase.child(chosenScenario).child(chosenRoom).child("p2locked");
        } else {
            getDBChoiceLocked =
                    mDatabase.child(chosenScenario).child(chosenRoom).child("p1locked");
        }

        ValueEventListener playerLockedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isPlayer1) {
                    currentRoom.p2locked = ((boolean) dataSnapshot.getValue());
                } else {
                    currentRoom.p1locked = ((boolean) dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        getDBChoiceLocked.addValueEventListener(playerLockedListener);
        if (isPlayer1) {
            while (!currentRoom.p2locked) ;
            return currentRoom.p2locked;
        }
        while (!currentRoom.p1locked) ;
        return currentRoom.p1locked;

    }

    public void lockChoice() {
        String currentPlayer = "";
        if (isPlayer1) {
            currentPlayer = "p1locked";
        } else {
            currentPlayer = "p2locked";
        }
        DatabaseReference getDBLockChoice =
                mDatabase.child(chosenScenario).child(chosenRoom).child(currentPlayer);
        getDBLockChoice.setValue(true);
    }

    public void incrementWin() {
        int wins = thisPlayer.getWins();
        ++wins;
        thisPlayer.setWins(wins);
        DatabaseReference currentPlayer;
        if (isPlayer1) {
            currentPlayer =
                    mDatabase.child("users").child(currentRoom.player1).child("wins");
        } else {
            currentPlayer =
                    mDatabase.child("users").child(currentRoom.player2).child("wins");
        }
        currentPlayer.setValue(wins);
    }

    public void incrementLoss() {
        int losses = thisPlayer.getWins();
        ++losses;
        thisPlayer.setLosses(losses);
        DatabaseReference currentPlayer;
        if (isPlayer1) {
            currentPlayer =
                    mDatabase.child("users").child(currentRoom.player1).child("losses");
        } else {
            currentPlayer =
                    mDatabase.child("users").child(currentRoom.player2).child("losses");
        }
        currentPlayer.setValue(losses);
    }

    public void finishGame() {
        RoomStatus doneRoom = new RoomStatus();
        currentRoom = doneRoom;
        DatabaseReference setDBRoomDone =
                mDatabase.child(chosenScenario).child(chosenRoom);
        setDBRoomDone.setValue(doneRoom);
    }

}
