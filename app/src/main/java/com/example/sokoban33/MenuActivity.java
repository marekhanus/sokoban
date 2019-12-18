package com.example.sokoban33;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void newame(View view) {
        Intent mainActivity = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainActivity);
    }

    public void levels(View view) {
        Intent levelsActivity = new Intent(getBaseContext(), LevelsActivity.class);
        startActivity(levelsActivity);
    }

    public void settings(View view) {

        Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(settingsActivity);
    }
}
