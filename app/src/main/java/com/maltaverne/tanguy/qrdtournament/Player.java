package com.maltaverne.tanguy.qrdtournament;

/**
 * Created by tanguy on 12/11/16.
 */

public class Player {

    private String name;
    private int score;
    private boolean titleHolder;

    public Player(String inputName, int inputScore, boolean inputTitleHolder) {
        name = inputName;
        score = inputScore;
        titleHolder = inputTitleHolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String inputName) {
        name = inputName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int inputScore) {
        score = inputScore;
    }

    public boolean getTitleHolder() {
        return titleHolder;
    }

    public void setTitleHolder(boolean inputTitleHolder) {
        titleHolder = inputTitleHolder;
    }
}
