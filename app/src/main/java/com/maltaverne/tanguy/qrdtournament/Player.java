package com.maltaverne.tanguy.qrdtournament;

/**
 * Created by tanguy on 12/11/16.
 */

public class Player {

    private String mName;
    private int mInitialScore;
    private int mScore;
    private boolean mTitleHolder;

    public Player(String inputName, int inputScore, boolean inputTitleHolder) {
        mName = inputName;
        mInitialScore = inputScore;
        mScore = inputScore;
        mTitleHolder = inputTitleHolder;
    }

    public String getName() {
        return mName;
    }

    public void setName(String inputName) {
        mName = inputName;
    }

    public int getInitialScore() {
        return mInitialScore;
    }

    public void setInitialScore(int inputScore) {
        mInitialScore = inputScore;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int inputScore) {
        mScore = inputScore;
    }

    public boolean getTitleHolder() {
        return mTitleHolder;
    }

    public void setTitleHolder(boolean inputTitleHolder) {
        mTitleHolder = inputTitleHolder;
    }
}
