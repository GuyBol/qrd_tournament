package com.maltaverne.tanguy.qrdtournament;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.widget.TextView.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        addScoreRow(scoreTable, "Toto", 0, false);
        addScoreRow(scoreTable, "Titi", 0, false);
    }

    private void addScoreRow(TableLayout scoreTable, String name, int score, boolean titleHolder) {
        TableRow row = new TableRow(this);
        scoreTable.addView(row);
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
        row.addView(checkBox);
    }
}