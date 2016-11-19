package com.maltaverne.tanguy.qrdtournament;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Tournament mTournament = new Tournament();
    boolean mDeletePlayer = false;

    private TableLayout mScoreTable;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScoreTable = (TableLayout) findViewById(R.id.scoreTable);

        load();
        buildScoreList();

        // Button to switch to games activity
        Button gamesButton = (Button) findViewById(R.id.gamesButton);
        gamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GamesActivity.class);
                Bundle paramBundle = new Bundle();
                paramBundle.putStringArrayList("players_names", mTournament.getPlayersNames());
                intent.putExtras(paramBundle);
                startActivityForResult(intent, 0);
            }
        });

        // Button to add a player
        final Button addPlayerButton = (Button) findViewById(R.id.addPlayerButton);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow addPlayerRow = (TableRow) findViewById(R.id.addPlayerRow);
                // At first display the add player row
                if (addPlayerRow.getVisibility() != View.VISIBLE) {
                    addPlayerRow.setVisibility(View.VISIBLE);
                    EditText nameInput = (EditText) findViewById(R.id.nameInput);
                    nameInput.setText("");
                }
                else {
                    Player player = getNewPlayer();
                    if (!mDeletePlayer) {
                        if (player.getName().isEmpty()) {
                            addPlayerRow.setVisibility(View.GONE);
                            return;
                        }
                        mTournament.addPlayer(player);
                    }
                    else {
                        Player playerToDelete = mTournament.findPlayer(player.getName());
                        if (playerToDelete.getName().isEmpty()) {
                            return;
                        }
                        mTournament.removePlayer(playerToDelete);
                    }
                    addPlayerRow.setVisibility(View.GONE);
                    EditText nameInput = (EditText) findViewById(R.id.nameInput);
                    nameInput.setText("");
                    buildScoreList();
                    save();
                }
            }
        });

        // Detect activities on new player name field, to be able to change button to delete it
        final EditText nameInput = (EditText) findViewById(R.id.nameInput);
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Player selectedPlayer = mTournament.findPlayer(nameInput.getText().toString());
                if (!selectedPlayer.getName().isEmpty()) {
                    addPlayerButton.setText(R.string.delete_player);
                    mDeletePlayer = true;
                }
                else {
                    addPlayerButton.setText(R.string.add_player);
                    mDeletePlayer = false;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Reset button (for debug purposes)
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTournament.reset();
                buildScoreList();
                save();
            }
        });
    }


    // When we are back from games activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            Log.d("MainActivity", "Return from GamesActivity");
            ArrayList<String> winners = data.getExtras().getStringArrayList("winners");
            Game newGame = new Game();
            for (String winner : winners) {
                Log.d("MainActivity", "Winner: " + winner);
                Player winnerPlayer = mTournament.findPlayer(winner);
                newGame.addWinner(winnerPlayer);
            }
            mTournament.addGame(newGame);
            // Rebuild updated score list
            buildScoreList();
            // Save result
            save();
        }
    }


    private void buildScoreList() {
        // Clean list before rebuilding it
        int rowNumber = mScoreTable.getChildCount();
        if (rowNumber > 1) {
            mScoreTable.removeViews(1, rowNumber - 2);
        }
        for (Player player : mTournament.getPlayers()) {
            addScoreRow(mScoreTable, player.getName(), player.getScore(), player.getTitleHolder());
        }
    }

    private void addScoreRow(TableLayout scoreTable, String name, int score, boolean titleHolder) {
        int rowNumber = scoreTable.getChildCount();
        TableRow row = new TableRow(this);
        scoreTable.addView(row, rowNumber-1);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setLayoutParams(layoutParams);
        row.addView(nameText);
        TextView scoreText = new TextView(this);
        scoreText.setText(String.valueOf(score));
        scoreText.setLayoutParams(layoutParams);
        row.addView(scoreText);
        CheckBox checkBox = new CheckBox(this);
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setClickable(false);
        checkBox.setChecked(titleHolder);
        row.addView(checkBox);
    }

    // Get the input data for the player to be added
    private Player getNewPlayer() {
        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        String name = nameInput.getText().toString();
        EditText scoreInput = (EditText) findViewById(R.id.scoreInput);
        int score = 0;
        if (scoreInput.getText().length() > 0) {
            score = Integer.parseInt(scoreInput.getText().toString());
        }
        CheckBox titleHolderInput = (CheckBox) findViewById(R.id.titleHolderInput);
        boolean titleHolder = titleHolderInput.isChecked();
        return new Player(name, score, titleHolder);
    }

    // Save current game locally
    private void save() {
        try {
            FileOutputStream outputStream = openFileOutput("save.xml", Context.MODE_PRIVATE);
            mTournament.save(outputStream);
            outputStream.close();

            // Dump file in logs
            FileInputStream fis = openFileInput("save.xml");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("XML dump", line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the saved game
    private void load() {
        String data = null;
        try {
            FileInputStream fis = openFileInput("save.xml");
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            isr.close();
            fis.close();
            mTournament.load(data);
        } catch (FileNotFoundException e) {
            Log.d("MainActivity", "File not found, just ignoring and start with a new tournament");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}