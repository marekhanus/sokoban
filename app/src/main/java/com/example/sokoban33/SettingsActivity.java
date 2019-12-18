package com.example.sokoban33;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = getSharedPreferences(
                "com.example.sokoban33",
                android.content.Context.MODE_PRIVATE
        );
        boolean sound = preferences.getBoolean("sound", true);

        Switch soundSwitch = findViewById(R.id.switch1);
        soundSwitch.setChecked(sound);
        soundSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                        "com.example.sokoban33",
                        android.content.Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sound", b);
                editor.commit();
            }
        });
    }
}
