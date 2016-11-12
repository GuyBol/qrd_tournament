package com.maltaverne.tanguy.qrdtournament;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Tournament tournament = new Tournament();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TableLayout scoreTable = (TableLayout) findViewById(R.id.scoreTable);

        buildScoreList(scoreTable, tournament.getPlayers());

        Button startGame = (Button) findViewById(R.id.addPlayerButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow addPlayerRow = (TableRow) findViewById(R.id.addPlayerRow);
                // At first display the add player row
                if (addPlayerRow.getVisibility() != View.VISIBLE) {
                    addPlayerRow.setVisibility(View.VISIBLE);
                }
                else {
                    Player player = getNewPlayer();
                    tournament.addPlayer(player);
                    addPlayerRow.setVisibility(View.GONE);
                    buildScoreList(scoreTable, tournament.getPlayers());
                }
            }
        });
    }

    private void buildScoreList(TableLayout scoreTable, ArrayList<Player> players) {
        // Clean list before rebuilding it
        int rowNumber = scoreTable.getChildCount();
        if (rowNumber > 1) {
            scoreTable.removeViews(1, rowNumber - 2);
        }
        for (Player player : players) {
            addScoreRow(scoreTable, player.getName(), player.getScore(), player.getTitleHolder());
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
        int score = Integer.parseInt(scoreInput.getText().toString());
        CheckBox titleHolderInput = (CheckBox) findViewById(R.id.titleHolderInput);
        boolean titleHolder = titleHolderInput.isChecked();
        return new Player(name, score, titleHolder);
    }
}