package com.maltaverne.tanguy.qrdtournament;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tanguy on 12/11/16.
 */

public class Game {

    private ArrayList<Player> mParticipants;
    private ArrayList<Player> mWinners;

    public Game() {
        mParticipants = new ArrayList<Player>();
        mWinners = new ArrayList<Player>();
    }

    public void addParticipant(Player player) {
        mParticipants.add(player);
    }

    public void addWinner(Player player) {
        mWinners.add(player);
    }

    public void updatePlayers() {
        for (Player winner : mWinners) {
            winner.setScore(winner.getScore() + 1);
            winner.setTitleHolder(true);
        }
    }
}
