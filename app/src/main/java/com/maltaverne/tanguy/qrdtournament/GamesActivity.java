package com.maltaverne.tanguy.qrdtournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GamesActivity extends AppCompatActivity {

    private HashMap<String, CheckBox> mNewGameCheckBoxes = new HashMap<String, CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ArrayList<String> playerNames = this.getIntent().getExtras().getStringArrayList("players_names");
        createNewGameTable(playerNames);

        Button addGameButton = (Button) findViewById(R.id.addGameButton);
        if (playerNames.isEmpty()) {
            addGameButton.setVisibility(View.GONE);
        }
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> winners = findWinners();
                if (!winners.isEmpty()) {
                    Intent returnData = new Intent();
                    Bundle resultBundle = new Bundle();
                    resultBundle.putStringArrayList("winners", winners);
                    returnData.putExtras(resultBundle);
                    setResult(RESULT_OK, returnData);
                    finish();
                }
            }
        });

        Button scoresButton = (Button) findViewById(R.id.scoresButton);
        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void createNewGameTable(ArrayList<String> playersNames) {
        TableLayout newGameTable = (TableLayout) findViewById(R.id.newGameTable);
        for (String name : playersNames) {
            addPlayerToNewGameTable(newGameTable, name);
        }
    }


    private void addPlayerToNewGameTable(TableLayout newGameTable, String playerName) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(layoutParams);
        deleteButton.setText("X");
        row.addView(deleteButton);

        newGameTable.addView(row);
        TextView nameTextView = new TextView(this);
        nameTextView.setLayoutParams(layoutParams);
        nameTextView.setText(playerName);
        row.addView(nameTextView);

        CheckBox winCheckBox = new CheckBox(this);
        winCheckBox.setLayoutParams(layoutParams);
        row.addView(winCheckBox);
        mNewGameCheckBoxes.put(playerName, winCheckBox);
    }


    private ArrayList<String> findWinners() {
        ArrayList<String> winners = new ArrayList<String>();
        for (HashMap.Entry<String, CheckBox> entry : mNewGameCheckBoxes.entrySet()) {
            if (entry.getValue().isChecked()) {
                winners.add(entry.getKey());
                Log.d("GamesActivity", "Winner: " + entry.getKey());
            }
        }
        return winners;
    }
}
