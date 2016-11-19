package com.maltaverne.tanguy.qrdtournament;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tanguy on 12/11/16.
 */

public class Tournament {

    private ArrayList<Player> mPlayers;
    private ArrayList<Game> mGames;

    public Tournament() {
        reset();
    }

    public void reset() {
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


    public void save(FileOutputStream outputStream) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(outputStream, "UTF-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag(null, "QRDTournament");

        serializer.startTag(null, "Participants");
        for (Player player : mPlayers) {
            serializer.startTag(null, "Player");
            serializer.attribute(null, "name", player.getName());
            serializer.attribute(null, "initial_score", String.valueOf(player.getInitialScore()));
            serializer.endTag(null, "Player");
        }
        serializer.endTag(null, "Participants");

        serializer.startTag(null, "Games");
        for (Game game : mGames) {
            serializer.startTag(null, "Game");
            serializer.startTag(null, "Participants");
            for (Player player : game.getParticipants()) {
                serializer.startTag(null, "Player");
                serializer.attribute(null, "name", player.getName());
                serializer.endTag(null, "Player");
            }
            serializer.endTag(null, "Participants");
            serializer.startTag(null, "Winners");
            for (Player winner : game.getWinners()) {
                serializer.startTag(null, "Player");
                serializer.attribute(null, "name", winner.getName());
                serializer.endTag(null, "Player");
            }
            serializer.endTag(null, "Winners");
            serializer.endTag(null, "Game");
        }
        serializer.endTag(null, "Games");

        serializer.endTag(null, "QRDTournament");
        serializer.endDocument();

        serializer.flush();
    }


    public boolean load(String data) {
        Log.d("Xml parser", data);
        reset();
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        }
        catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            return false;
        }
        try {
            xpp.setInput(new StringReader(data));
        }
        catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        }
        catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
        while (eventType != XmlPullParser.END_DOCUMENT){
            try {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Xml parser", "Start document");
                }
                else if (eventType == XmlPullParser.START_TAG) {
                    Log.d("Xml parser", "Start tag "+xpp.getName());
                    if (xpp.getName().equals("QRDTournament")) {
                        if (!parseTournament(xpp)) {
                            return false;
                        }
                    }
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    Log.d("Xml parser", "End tag "+xpp.getName());
                }
                else if(eventType == XmlPullParser.TEXT) {
                    //userData.add(xpp.getText());
                }
                eventType = xpp.next();
            }
            catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean parseTournament(XmlPullParser xpp) throws IOException, XmlPullParserException {
        Log.d("Xml parser", "parseTournament");
        int eventType = xpp.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                Log.d("Xml parser", "Start tag "+xpp.getName());
                if (xpp.getName().equals("Participants")) {
                    ArrayList<Player> participants = new ArrayList<>();
                    if (!parsePlayers(xpp, participants)) {
                        return false;
                    }
                    mPlayers = participants;
                }
                else if (xpp.getName().equals("Games")) {
                    if (!parseGames(xpp)) {
                        return false;
                    }
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                Log.d("Xml parser", "End tag "+xpp.getName());
                if (xpp.getName().equals("QRDTournament")) {
                    return true;
                }
            }
            eventType = xpp.next();
        }
        return false;
    }

    private boolean parsePlayers(XmlPullParser xpp, ArrayList<Player> players) throws IOException, XmlPullParserException {
        Log.d("Xml parser", "parsePlayers");
        int eventType = xpp.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("Player")) {
                    String name = xpp.getAttributeValue(null, "name");
                    if (name == null) {
                        return false;
                    }
                    String initialScore = xpp.getAttributeValue(null, "initial_score");
                    if (initialScore == null) {
                        initialScore = new String("0");
                    }
                    Player player = new Player(name, Integer.parseInt(initialScore), false);
                    players.add(player);
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                // If we end a tag which is not a player, it means that we have ended the players list
                if (!xpp.getName().equals("Player")) {
                    return true;
                }
            }
            eventType = xpp.next();
        }
        return false;
    }

    private boolean parseGames(XmlPullParser xpp) throws IOException, XmlPullParserException {
        Log.d("Xml parser", "parseGames");
        int eventType = xpp.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                Log.d("Xml parser", "Start tag "+ xpp.getName());
                if (xpp.getName().equals("Game")) {
                    Game game = new Game();
                    if (!parseGame(xpp, game)) {
                        return false;
                    }
                    addGame(game);
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (xpp.getName().equals("Games")) {
                    return true;
                }
            }
            eventType = xpp.next();
        }
        return false;
    }

    private boolean parseGame(XmlPullParser xpp, Game game) throws IOException, XmlPullParserException {
        Log.d("Xml parser", "parseGame");
        int eventType = xpp.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("Participants")) {
                    Log.d("Xml parser", "Parse game participants");
                    ArrayList<Player> participants = new ArrayList<>();
                    if (!parsePlayers(xpp, participants)) {
                        Log.d("Xml parser", "Can't parse game participants");
                        return false;
                    }
                    for (Player participant : participants) {
                        game.addParticipant(findPlayer(participant.getName()));
                    }
                }
                else if (xpp.getName().equals("Winners")) {
                    Log.d("Xml parser", "Parse game winners");
                    ArrayList<Player> winners = new ArrayList<>();
                    if (!parsePlayers(xpp, winners)) {
                        Log.d("Xml parser", "Can't parse game participants");
                        return false;
                    }
                    for (Player winner : winners) {
                        Log.d("Xml parser", "Add winner: " + winner.getName());
                        game.addWinner(findPlayer(winner.getName()));
                    }
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                Log.d("Xml parser", "End tag in parseGame" + xpp.getName());
                if (xpp.getName().equals("Game")) {
                    Log.d("Xml parser", "parseGame end");
                    return true;
                }
            }
            eventType = xpp.next();
        }
        return false;
    }
}
