package com.example.memeinnovations.gameofscenarios.data;

import java.io.Serializable;

/**
 * Created by Vincent xD on 10/11/2017.
 * For storing users in firebase
 */

public class User implements Serializable {
    private boolean anonymous;
    private String userId;
    private String userName;
    private String gender;
    private String age;
    private String ethnicity;
    private int reRolls;
    private int wins;
    private int losses;
    private int totalGamesPlayed;

    public User(User rightUser) {
        this.userId = rightUser.userId;
        this.userName = rightUser.userName;
        this.gender = rightUser.gender;
        this.age = rightUser.age;
        this.ethnicity = rightUser.ethnicity;
        this.reRolls = rightUser.reRolls;
        this.wins = rightUser.wins;
        this.losses = rightUser.losses;
        this.totalGamesPlayed = rightUser.totalGamesPlayed;
    }

    public User(String userId, String userName, String gender, String age, String ethnicity) {
        reRolls = 0;
        wins = 0;
        losses = 0;
        totalGamesPlayed = 0;
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.ethnicity = ethnicity;
        anonymous = false;
    }

    public User() {
        reRolls = 0;
        wins = 0;
        losses = 0;
        totalGamesPlayed = 0;
        userId = "";
        userName = "";
        gender = "";
        age = "";
        ethnicity = "";
        anonymous = true;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public int getReRolls() {
        return reRolls;
    }

    public void setReRolls(int reRolls) {
        this.reRolls = reRolls;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
