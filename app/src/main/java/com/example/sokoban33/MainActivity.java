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
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                Log.d("getX", Float.toString(x));
                Log.d("getY", Float.toString(y));
                Log.d("sizeX", Float.toString(view.getWidth()));
                Log.d("sizey", Float.toString(view.getHeight()));
                return false;
            }
        });
    }
}
