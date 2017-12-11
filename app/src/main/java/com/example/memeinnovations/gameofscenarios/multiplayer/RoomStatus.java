package com.example.memeinnovations.gameofscenarios.multiplayer;

import java.io.Serializable;

/**
 * Created by Vincent on 12/5/2017.
 */

public class RoomStatus implements Serializable {
    public boolean in_use;
    public boolean rerolled;
    public String p1choice;
    public boolean p1connected;
    public boolean p1locked;
    public boolean p1ready;
    public String p2choice;
    public boolean p2connected;
    public boolean p2locked;
    public boolean p2ready;
    public String player1;
    public String player2;
    public String rerolledScenario;
    public String rerolledRoom;

    public RoomStatus() {
        in_use = false;
        rerolled = false;
        p1choice = "";
        p1connected = false;
        p1locked = false;
        p1ready = false;
        p2choice = "";
        p2connected = false;
        p2locked = false;
        p2ready = false;
        player1 = "";
        player2 = "";
        rerolledScenario = "";
        rerolledRoom = "";
    }

    public RoomStatus(RoomStatus otherRoom) {
        in_use = otherRoom.in_use;
        rerolled = otherRoom.rerolled;
        p1choice = otherRoom.p1choice;
        p1connected = otherRoom.p1connected;
        p1locked = otherRoom.p1locked;
        p1ready = otherRoom.p1ready;
        p2choice = otherRoom.p2choice;
        p2connected = otherRoom.p2connected;
        p2locked = otherRoom.p2locked;
        p2ready = otherRoom.p2ready;
        player1 = otherRoom.player1;
        player2 = otherRoom.player2;
        rerolledScenario = otherRoom.rerolledScenario;
        rerolledRoom = otherRoom.rerolledRoom;
    }
}
