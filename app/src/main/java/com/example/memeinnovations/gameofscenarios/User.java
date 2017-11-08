package com.example.memeinnovations.gameofscenarios;

/**
 * Created by Vincent xD on 10/11/2017.
 */

public class User {
    private String userName;
    private String gender;
    private String age;
    private String ethnicity;
    private int reRolls;
    private int wins;
    private int losses;

    public User(String userName, String gender, String age, String ethnicity) {
        reRolls = 0;
        wins = 0;
        losses = 0;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.ethnicity = ethnicity;
    }

    public User() {
        reRolls = 0;
        wins = 0;
        losses = 0;
        userName = "";
        gender = "";
        age = "";
        ethnicity = "";
    }

    public String getUsername(){ return userName; }
    public void setUsername(String userName){
        this.userName = userName;
    }

    public String getGender() { return gender; }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() { return age; }
    public void setAge(String age) {
        this.age = age;
    }

    public String getEthnicity() { return ethnicity; }
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
}
