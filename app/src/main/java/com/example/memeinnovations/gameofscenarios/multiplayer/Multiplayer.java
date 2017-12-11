package com.example.memeinnovations.gameofscenarios.multiplayer;

import android.widget.Toast;


import com.example.memeinnovations.gameofscenarios.MApplication;
import com.example.memeinnovations.gameofscenarios.R;
import com.example.memeinnovations.gameofscenarios.data.FirebaseDB;
import com.example.memeinnovations.gameofscenarios.data.User;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Vincent on 11/19/2017.
 * Class for implementing all the multiplayer features
 * Talks to the Firebase Realtime Database
 */

public class Multiplayer implements Serializable  {
    // store user information in an object to obtain from the database later.
    private User thisPlayer;

    // stores if current player is player 1 or player 2
    private boolean isPlayer1;

    // sets if a room was found or not
    private boolean foundRoom;
    private RoomStatus currentRoom;

    // set other player's choice
    private String otherPlayersChoice;

    // sets chosen scenario
    private String chosenScenario;

    // sets chosen room
    private String chosenRoom;

    // iterator for chooseRoom() function
    private int roomIterator;

    private boolean leftQueue;
    private boolean thisPlayerRerolled;


    public Multiplayer() {
        currentRoom = new RoomStatus();
        foundRoom = false;
        isPlayer1 = false;
        chosenScenario = "";
        chosenRoom = "";
        otherPlayersChoice = "";
        roomIterator = 0;
        leftQueue = false;
        thisPlayerRerolled = false;
    }

    public String getChosenScenario() {
        return chosenScenario;
    }
    public String getOtherPlayersChoice() { return otherPlayersChoice; }

    private void getPlayer(final TaskCompletionSource<Void> waitSource) {
        DatabaseReference currentPlayer;
        if (isPlayer1) {
            currentPlayer =
                    FirebaseDB.mDatabase.child("users").child(currentRoom.player1);
        } else {
            currentPlayer =
                    FirebaseDB.mDatabase.child("users").child(currentRoom.player2);
        }

        currentPlayer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    waitSource.trySetResult(null);
                    thisPlayer = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void chooseScenario() {
        String[] array =
                MApplication.getAppContext().getResources().getStringArray(R.array.scenarios);
        chosenScenario = array[new Random().nextInt(array.length)];
    }

    private void chooseRoom(final TaskCompletionSource<Void> waitSource) {
        final String[] array =
                MApplication.getAppContext().getResources().getStringArray(R.array.rooms);
        final DatabaseReference checkRoom =
                FirebaseDB.mDatabase.child(chosenScenario);
        checkRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot inUseSnap : dataSnapshot.getChildren()) {
                    // check if room is in-use and if a player is needed or not
                    currentRoom.in_use = (boolean) inUseSnap.child("in_use").getValue();
                    currentRoom.p1connected = (boolean) inUseSnap.child("p1connected").getValue();
                    currentRoom.p2connected = (boolean) inUseSnap.child("p2connected").getValue();
                    callBackChooseRoom(array, checkRoom);

                    if (foundRoom) {
                        // connect players using another callback.
                        connectPlayers(waitSource);
                        // break the foreach loop when a room is found.
                        break;
                    }
                    // iterator through list
                    roomIterator = (roomIterator + 1) % array.length;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void callBackChooseRoom(String[] roomsArray,
                                    DatabaseReference pCheckRoom) {
        if (!currentRoom.in_use || !(currentRoom.p1connected && currentRoom.p2connected)) {
            currentRoom.in_use = true;
            foundRoom = true;
            chosenRoom = roomsArray[roomIterator];
            pCheckRoom.child(roomsArray[roomIterator]).child("in_use").setValue(true);

            if (!currentRoom.p1connected) {
                currentRoom.p1connected = true;
                isPlayer1 = true;
                pCheckRoom.child(roomsArray[roomIterator]).child("p1connected").setValue(true);
                if (FirebaseDB.mAuth.getCurrentUser() != null) {
                    currentRoom.player1 = FirebaseDB.mAuth.getCurrentUser().getUid();
                    pCheckRoom.child(roomsArray[roomIterator]).child("player1")
                            .setValue(FirebaseDB.mAuth.getCurrentUser().getUid());
                }
            } else {
                currentRoom.p2connected = true;
                pCheckRoom.child(roomsArray[roomIterator]).child("p2connected").setValue(true);
                if (FirebaseDB.mAuth.getCurrentUser() != null) {
                    currentRoom.player2 = FirebaseDB.mAuth.getCurrentUser().getUid();
                    pCheckRoom.child(roomsArray[roomIterator]).child("player2")
                            .setValue(FirebaseDB.mAuth.getCurrentUser().getUid());
                }
            }
        }
    }

    private void connectPlayers(final TaskCompletionSource<Void> waitSource) {
        final DatabaseReference connectPlayers =
                FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom);

        connectPlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isPlayer1) {
                    currentRoom.p2connected = (boolean) dataSnapshot.child("p2connected").getValue();
                    currentRoom.player2 = dataSnapshot.child("player2").getValue(String.class);
                } else {
                    currentRoom.p1connected = (boolean) dataSnapshot.child("p1connected").getValue();
                    currentRoom.player1 = dataSnapshot.child("player1").getValue(String.class);
                }
                if (!currentRoom.p2connected || !currentRoom.p1connected) {
                    if (!leftQueue) {
                        Toast.makeText(MApplication.getAppContext(),
                                "Searching for other player, please wait....",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // retrieve player from database
                    getPlayer(waitSource);
                    connectPlayers.removeEventListener(this);
                    Toast.makeText(MApplication.getAppContext(), "Player found!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void quickPlay(final TaskCompletionSource<Void> waitSource) {
        chooseScenario();
        chooseRoom(waitSource);
    }

    public void makeChoice(String choice) {
        String currentPlayer;
        if (isPlayer1) {
            currentRoom.p1choice = choice;
            currentPlayer = "p1choice";
        } else {
            currentRoom.p2choice = choice;
            currentPlayer = "p2choice";
        }
        DatabaseReference thisPlayer =
                FirebaseDB.mDatabase.child(chosenScenario)
                        .child(chosenRoom).child(currentPlayer);
        thisPlayer.setValue(choice);
    }

    public void checkOtherPlayersChoiceLocked(final TaskCompletionSource<Void> waitSource) {
        final DatabaseReference lockPlayers =
                FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom);
        lockPlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isPlayer1) {
                    currentRoom.p2locked =
                            ((boolean) dataSnapshot.child("p2locked").getValue());
                } else {
                    currentRoom.p1locked =
                            ((boolean) dataSnapshot.child("p1locked").getValue());
                }
                if (!currentRoom.p1locked || !currentRoom.p2locked) {
                    Toast.makeText
                            (MApplication.getAppContext(),
                                    "Other player is making choice....",
                                    Toast.LENGTH_SHORT).show();
                } else {
                    if (isPlayer1) {
                        currentRoom.p2choice =
                                (String) dataSnapshot.child("p2choice").getValue();
                        otherPlayersChoice = currentRoom.p2choice;
                    } else {
                        currentRoom.p1choice =
                                (String) dataSnapshot.child("p1choice").getValue();
                        otherPlayersChoice = currentRoom.p1choice;
                    }
                    lockPlayers.removeEventListener(this);
                    waitSource.trySetResult(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void lockChoice() {
        String currentPlayer = "";
        if (isPlayer1) {
            currentRoom.p1locked = true;
            currentPlayer = "p1locked";
        } else {
            currentRoom.p2locked = true;
            currentPlayer = "p2locked";
        }
        DatabaseReference getDBLockChoice =
                FirebaseDB.mDatabase
                        .child(chosenScenario).child(chosenRoom).child(currentPlayer);
        getDBLockChoice.setValue(true);
    }

    public void incrementWin(int rewardPoints) {
        DatabaseReference currentPlayer;
        int wins = 0;

        if (thisPlayer != null) {
            wins = thisPlayer.getWins();
            ++wins;
            rewardPoints = thisPlayer.getRewardPoints() + rewardPoints;
        }
        if (isPlayer1) {
            if (currentRoom.player1 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("wins");
                currentPlayer.setValue(wins);
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("rewardPoints");
                currentPlayer.setValue(rewardPoints);
            }
        } else {
            if (currentRoom.player2 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player2).child("wins");
                currentPlayer.setValue(wins);
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("rewardPoints");
                currentPlayer.setValue(rewardPoints);
            }
        }
    }

    public void incrementDraws(int rewardPoints) {
        DatabaseReference currentPlayer;
        int draws = 0;

        if (thisPlayer != null){
            draws = thisPlayer.getDraws();
            ++draws;
            rewardPoints = thisPlayer.getRewardPoints() + rewardPoints;
        }
        if (isPlayer1) {
            if (currentRoom.player1 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("draws");
                currentPlayer.setValue(draws);
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("rewardPoints");
                currentPlayer.setValue(rewardPoints);
            }
        } else {
            if (currentRoom.player2 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player2).child("draws");
                currentPlayer.setValue(draws);
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("rewardPoints");
                currentPlayer.setValue(rewardPoints);

            }
        }

    }

    public void incrementLoss(int rewardPoints) {
        DatabaseReference currentPlayer;
        int losses = 0;

        if (thisPlayer != null) {
            losses = thisPlayer.getLosses();
            ++losses;
            rewardPoints = thisPlayer.getRewardPoints() + rewardPoints;
        }
        if (isPlayer1) {
            if (currentRoom.player1 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("losses");
                currentPlayer.setValue(losses);
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("rewardPoints");
                currentPlayer.setValue(rewardPoints);
            }
        } else {
            if (currentRoom.player2 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player2).child("losses");
                currentPlayer.setValue(losses);
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("rewardPoints");
                currentPlayer.setValue(rewardPoints);

            }
        }
    }

    public void incrementTotalGames() {
        DatabaseReference currentPlayer;
        int totalGames = 0;

        if (thisPlayer != null) {
            totalGames = thisPlayer.getTotalGamesPlayed();
            ++totalGames;
        }
        if (isPlayer1) {
            if (currentRoom.player1 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player1).child("totalGamesPlayed");
                currentPlayer.setValue(totalGames);
            }
        } else {
            if (currentRoom.player2 != null) {
                currentPlayer =
                        FirebaseDB.mDatabase.child("users")
                                .child(currentRoom.player2).child("totalGamesPlayed");
                currentPlayer.setValue(totalGames);
            }
        }
    }

    public void finishGame() {
        currentRoom.in_use = false;
        currentRoom.p1choice = "";
        currentRoom.p1connected = false;
        currentRoom.p1locked = false;
        currentRoom.p1ready = false;
        currentRoom.p2choice = "";
        currentRoom.p2connected = false;
        currentRoom.p2locked = false;
        currentRoom.p2ready = false;
        currentRoom.player1 = "";
        currentRoom.player2 = "";
        currentRoom.rerolledRoom = "";
        currentRoom.rerolledScenario = "";

        DatabaseReference setDBRoomDone =
                FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom);
        setDBRoomDone.setValue(currentRoom);
    }

    public void rerollScenario(final TaskCompletionSource<Void> waitSource) {
        foundRoom = false;
        roomIterator = 0;
        chooseRerolledRoom(waitSource);
    }

    private void chooseRerolledRoom(final TaskCompletionSource<Void> waitSource) {
        // reroll old scenario and store old scenario to let other user know in DB
        final String oldScenario = chosenScenario;
        while (oldScenario.equals(chosenScenario)) {
            chooseScenario();
        }

        // reroll old room and store old room to let other user know in DB
        final String oldRoomNumber = chosenRoom;

        // store old room to set new room values to old room's values
        final RoomStatus oldRoomStatus = new RoomStatus(currentRoom);

        // store database reference to the old room
        final DatabaseReference oldRoomDB = FirebaseDB.mDatabase.child(oldScenario).
                child(oldRoomNumber);

        final String[] roomArray =
                MApplication.getAppContext().getResources().getStringArray(R.array.rooms);
        final DatabaseReference checkRoom =
                FirebaseDB.mDatabase.child(chosenScenario);
        checkRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot inUseSnap : dataSnapshot.getChildren()) {
                    // check if room is in-use and if a player is needed or not
                    currentRoom.in_use = (boolean) inUseSnap.child("in_use").getValue();
                    currentRoom.p1connected = (boolean) inUseSnap.child("p1connected").getValue();
                    currentRoom.p2connected = (boolean) inUseSnap.child("p2connected").getValue();

                    if (!currentRoom.in_use && (!currentRoom.p1connected &&
                            !currentRoom.p2connected)) {
                        thisPlayerRerolled = true;
                        chosenRoom = roomArray[roomIterator];
                        checkRoom.child(chosenRoom).setValue(oldRoomStatus);

                        // let the other user know what the other room is.
                        oldRoomDB.child("rerolled").setValue(true);
                        oldRoomDB.child("rerolledRoom").setValue(chosenRoom);
                        oldRoomDB.child("rerolledScenario").setValue(chosenScenario);
                        waitSource.setResult(null);
                        break;
                    }

                    // iterator through list
                    roomIterator = (roomIterator + 1) % roomArray.length;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void checkRerolled(final TaskCompletionSource<Void> waitSource) {
        final DatabaseReference checkRerolledDB =
                FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom);
        checkRerolledDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentRoom.rerolled = (boolean) dataSnapshot.child("rerolled").getValue();
                currentRoom.p1ready = (boolean) dataSnapshot.child("p1ready").getValue();
                currentRoom.p2ready = (boolean) dataSnapshot.child("p2ready").getValue();
                if (currentRoom.rerolled || (currentRoom.p1ready && currentRoom.p2ready)) {
                    // no need for this event listener if finished
                    checkRerolledDB.removeEventListener(this);
                    waitSource.trySetResult(null);
                    if (currentRoom.rerolled && !thisPlayerRerolled) {
                        Toast.makeText(MApplication.getAppContext(),"Other player rerolled!",
                                Toast.LENGTH_SHORT).show();
                        openRoomAfterReroll();
                        chosenScenario = dataSnapshot.child("rerolledScenario").getValue(String.class);
                        chosenRoom = dataSnapshot.child("rerolledRoom").getValue(String.class);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void openRoomAfterReroll() {
        RoomStatus openRoom = new RoomStatus();
        DatabaseReference setDBRoomDone =
                FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom);
        setDBRoomDone.setValue(openRoom);
    }

    public void setReady() {
        if (isPlayer1) {
            currentRoom.p1ready = true;
            FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom).
                    child("p1ready").setValue(true);
        } else {
            currentRoom.p2ready = true;
            FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom).
                    child("p2ready").setValue(true);
        }
    }

    public void checkReady(final TaskCompletionSource<Void> waitSource) {
        final DatabaseReference checkReadyDB =
                FirebaseDB.mDatabase.child(chosenScenario).child(chosenRoom);
        checkReadyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isPlayer1) {
                    currentRoom.p2ready =
                            ((boolean) dataSnapshot.child("p2ready").getValue());
                } else {
                    currentRoom.p1ready =
                            ((boolean) dataSnapshot.child("p1ready").getValue());
                }
                if (!currentRoom.p1ready || !currentRoom.p2ready) {
                    Toast.makeText
                            (MApplication.getAppContext(),
                                    "Other player isn't ready....",
                                    Toast.LENGTH_SHORT).show();
                } else {
                    checkReadyDB.removeEventListener(this);
                    waitSource.trySetResult(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onBackPressed() {
        leftQueue = true;
        // removes the person out of the game queue
        final DatabaseReference tempDB = FirebaseDB.
                mDatabase.child(chosenScenario).child(chosenRoom);
        if (isPlayer1) {
            currentRoom.player1 = "";
            currentRoom.p1connected = false;
            tempDB.child("player1").setValue("");
            tempDB.child("p1connected").setValue(false);
        } else {
            currentRoom.player2 = "";
            currentRoom.p2connected = false;
            tempDB.child("player2").setValue("");
            tempDB.child("p2connected").setValue(false);
        }

        // insures that the in_use boolean in the DB is false when both players have left
        tempDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(boolean) dataSnapshot.child("p1connected").getValue()
                        && !(boolean) dataSnapshot.child("p2connected").getValue()
                        && (boolean) dataSnapshot.child("in_use").getValue()) {
                    tempDB.child("in_use").setValue(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
