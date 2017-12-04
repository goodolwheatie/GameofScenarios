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
    int i;

    // boolean for checking if data was acquired from DB
    private Boolean dataAcquired;


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
        dataAcquired = false;
    }

    public Multiplayer(Context c) {
        currentRoom = new RoomStatus();
        foundRoom = false;
        isPlayer1 = false;
        chosenScenario = "";
        chosenRoom = "";
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataAcquired = false;
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
                    i = (i + 1) % array.length;

                    if (foundRoom) {
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
            chosenRoom = roomsArray[i];
            pCheckRoom.child(roomsArray[i]).child("in_use").setValue(true);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public String quickPlay() {
        chooseScenario();
        chooseRoom();
        // connectPlayers();
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
                    currentRoom.setP2locked((boolean) dataSnapshot.getValue());
                } else {
                    currentRoom.setP1locked((boolean) dataSnapshot.getValue());
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

    public class RoomStatus {
        private boolean in_use;
        private String p1choice;
        private boolean p1connected;
        private boolean p1locked;
        private String p2choice;
        private boolean p2connected;
        private boolean p2locked;
        private String player1;
        private String player2;

        public RoomStatus() {
            boolean in_use = false;
            String p1choice = "";
            boolean p1connected = false;
            boolean p1locked = false;
            String p2choice = "";
            boolean p2connected = false;
            boolean p2locked = false;
            String player1 = "";
            String player2 = "";
        }

        public boolean isIn_use() {
            return in_use;
        }

        public void setIn_use(boolean in_use) {
            this.in_use = in_use;
        }

        public String getP1choice() {
            return p1choice;
        }

        public void setP1choice(String p1choice) {
            this.p1choice = p1choice;
        }

        public boolean isP1connected() {
            return p1connected;
        }

        public void setP1connected(boolean p1connected) {
            this.p1connected = p1connected;
        }

        public boolean isP1locked() {
            return p1locked;
        }

        public void setP1locked(boolean p1locked) {
            this.p1locked = p1locked;
        }

        public String getP2choice() {
            return p2choice;
        }

        public void setP2choice(String p2choice) {
            this.p2choice = p2choice;
        }

        public boolean isP2connected() {
            return p2connected;
        }

        public void setP2connected(boolean p2connected) {
            this.p2connected = p2connected;
        }

        public boolean isP2locked() {
            return p2locked;
        }

        public void setP2locked(boolean p2locked) {
            this.p2locked = p2locked;
        }

        public String getPlayer1() {
            return player1;
        }

        public void setPlayer1(String player1) {
            this.player1 = player1;
        }

        public String getPlayer2() {
            return player2;
        }

        public void setPlayer2(String player2) {
            this.player2 = player2;
        }
    }
}
