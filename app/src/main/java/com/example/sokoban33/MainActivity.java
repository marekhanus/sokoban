package com.example.sokoban33;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

        sokoView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
