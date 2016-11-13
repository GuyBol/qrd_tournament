package com.maltaverne.tanguy.qrdtournament;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tanguy on 12/11/16.
 */

public class Tournament {

    private ArrayList<Player> mPlayers;
    private ArrayList<Game> mGames;

    public Tournament() {
        mPlayers = new ArrayList<>();
        mGames = new ArrayList<>();
    }

    public void addPlayer(Player newPlayer) {
        mPlayers.add(newPlayer);
    }

    public void removePlayer(Player playerToRemove) {
        for (Iterator<Player> iter = mPlayers.listIterator(); iter.hasNext(); ) {
            Player player = iter.next();
            if (player == playerToRemove) {
                iter.remove();
            }
        }
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public ArrayList<String> getPlayersNames() {
        ArrayList<String> array = new ArrayList<String>();
        for (Player player : mPlayers) {
            array.add(player.getName());
        }
        return array;
    }

    public Player findPlayer(String playerName) {
        for (Player player : mPlayers) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return new Player("", 0, false);
    }

    public void addGame(Game game) {
        // Clean the current title holder(s)
        for (Player player : mPlayers) {
            player.setTitleHolder(false);
        }
        // Update score based on game result
        game.updatePlayers();
        // Add game to the list
        mGames.add(game);
    }
}
