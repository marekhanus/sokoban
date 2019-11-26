package com.example.sokoban33;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SokoView sokoView = findViewById(R.id.sokoView);
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

    // Populates the activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
}
