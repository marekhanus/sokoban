package com.example.sokoban33;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public boolean USE_SWIPE_TOUCH_LISTENER = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SokoView sokoView = findViewById(R.id.sokoView);

        Intent intent = getIntent();
        String levelDefinition = intent.getStringExtra("LEVEL_DEFINITION");
        if (levelDefinition != null) {
            sokoView.redrawLevel(levelDefinition);
        }

        SharedPreferences preferences = getSharedPreferences(
                "com.example.sokoban33",
                android.content.Context.MODE_PRIVATE
        );
        boolean sound = preferences.getBoolean("sound", true);
        sokoView.setSound(sound);

        if (USE_SWIPE_TOUCH_LISTENER) {
            sokoView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                public void onSwipeTop() {
                    Log.d("touchPos", "top");
                    sokoView.moveTop();
                }
                public void onSwipeRight() {
                    Log.d("touchPos", "bottom");
                    sokoView.moveRight();
                }
                public void onSwipeLeft() {
                    Log.d("touchPos", "left");
                    sokoView.moveLeft();
                }
                public void onSwipeBottom() {
                    Log.d("touchPos", "right");
                    sokoView.moveBottom();
                }
            });
        } else {
            sokoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    float width = view.getWidth();
                    float height = view.getHeight();

                    Log.d("getX", Float.toString(x));
                    Log.d("getY", Float.toString(y));
                    Log.d("sizeX", Float.toString(width));
                    Log.d("sizey", Float.toString(height));

                    if (y < height * 0.25) {
                        Log.d("touchPos", "top");
                        sokoView.moveTop();
                    } else if (y > height * 0.75) {
                        Log.d("touchPos", "bottom");
                        sokoView.moveBottom();
                    } else if (x < width / 2) {
                        Log.d("touchPos", "left");
                        sokoView.moveLeft();
                    } else {
                        Log.d("touchPos", "right");
                        sokoView.moveRight();
                    }

                    return false;
                }
            });
        }
    }

    // Populates the activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    // Handles the user's menu selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.levels:
                Intent levelsActivity = new Intent(getBaseContext(), LevelsActivity.class);
                startActivity(levelsActivity);
                return true;
            case R.id.customLevel:
                Intent createLevelActivity = new Intent(getBaseContext(), CreateLevelActivity.class);
                startActivity(createLevelActivity);
            case R.id.settings:
                Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsActivity);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
