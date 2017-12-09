package com.example.memeinnovations.gameofscenarios.multiplayer;

import java.io.Serializable;

/**
 * Created by Vincent on 12/5/2017.
 */

public class RoomStatus implements Serializable {
    public boolean in_use;
    public String p1choice;
    public boolean p1connected;
    public boolean p1locked;
    public String p2choice;
    public boolean p2connected;
    public boolean p2locked;
    public String player1;
    public String player2;

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
}
