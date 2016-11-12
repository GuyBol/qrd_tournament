package com.maltaverne.tanguy.qrdtournament;

import java.util.ArrayList;

/**
 * Created by tanguy on 12/11/16.
 */

public class Tournament {

    private ArrayList<Player> players;

    public Tournament() {
        players = new ArrayList<Player>();
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
