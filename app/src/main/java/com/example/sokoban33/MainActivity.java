package com.example.sokoban33;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SokoView sokoView = findViewById(R.id.sokoView);
        sokoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("getX", Float.toString(motionEvent.getX()));
                Log.d("getY", Float.toString(motionEvent.getY()));
                return false;
            }
        });
    }
}
