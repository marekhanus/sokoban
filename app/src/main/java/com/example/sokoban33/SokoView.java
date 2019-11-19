package com.example.sokoban33;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kru13 on 12.10.16.
 */

public class SokoView extends View{

    Bitmap[] bmp;

    int lx = 10;
    int ly = 10;

    int width;
    int height;

    boolean standingOnGoal = false;

    private int level[] = {
            1,1,1,1,1,1,1,1,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,4,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,1,1,1,1,1,1,1,1,0,
            0,0,0,0,0,0,0,0,0,0
    };

    public SokoView(Context context) {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / ly;
        height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                canvas.drawBitmap(bmp[level[i*10 + j]], null,
                        new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
            }
        }

    }

    protected boolean move(int offset) {
        Integer index = null;

        // find cat current location
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                if (level[i*10 + j] == 4) {
                    index = i*10 + j;
                    Log.d("catch", String.valueOf(index));
                }
            }
        }

        // check if cat exists in area
        if (index == null) {
            return false;
        }

        // check if destination is in area range
        if (index + offset < 0 || index + offset >= 10 * 10) {
            return false;
        }

        // do not step left from left edge
        if (offset == -1 && index % 10 == 0) {
            return false;
        }

        // do not step right from right edge
        if (offset == 1 && index % 10 == 9) {
            return false;
        }

        // do not step on the wall
        if (level[index + offset] == 1) {
            return false;
        }

        // if in destination is box then move it
        if (level[index + offset] == 2) {
            // check if box destination is in area range
            if (index + offset * 2 < 0 || index + offset * 2 >= 10 * 10) {
                return false;
            }

            // do not move box on the wall or another box
            if (level[index + offset * 2] == 1 || level[index + offset * 2] == 2) {
                return false;
            }

            level[index + offset * 2] = 2;
        }

        // restore block on old cat location
        if (standingOnGoal) {
            level[index] = 3;
            standingOnGoal = false;
        } else {
            level[index] = 0;
        }

        // set new cat location
        level[index + offset] = 4;

        invalidate();

        return true;
    }

    public boolean moveTop() {
        return move(-10);
    }

    public boolean moveBottom() {
        return move(10);
    }

    public boolean moveLeft() {
        return move(-1);
    }

    public boolean moveRight() {
        return move(1);
    }
}