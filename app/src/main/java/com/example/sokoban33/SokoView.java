package com.example.sokoban33;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kru13 on 12.10.16.
 */

public class SokoView extends View{
    public static final int EMPTY = 0;
    public static final int WALL = 1;
    public static final int BOX = 2;
    public static final int GOAL = 3;
    public static final int HERO = 4;
    public static final int BOXOK = 5;

    Bitmap[] bmp;

    int lx = 10;
    int ly = 10;

    int width;
    int height;

    boolean standingOnGoal = false;

    private int[] level;

    public SokoView(Context context) throws IOException {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) throws IOException {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) throws IOException {
        bmp = new Bitmap[6];

        bmp[EMPTY] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[WALL] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[BOX] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[GOAL] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[HERO] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[BOXOK] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

        InputStream inputStream = context.getResources().openRawResource(R.raw.level);
        String current;
        int length = inputStream.available();
        level = new int[length];
        while (inputStream.available() > 0) {
            current = String.valueOf((char)inputStream.read());
            if (current.equals("\n") || current.equals(";")) {
                break;
            }

            level[length - inputStream.available() - 1] = Integer.parseInt(current);
        }

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "myTitle");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "mySubtitle");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    void redrawLevel(String levelDefinition) {
        for (int i = 0; i < levelDefinition.length(); i++) {
            level[i] = Character.getNumericValue(levelDefinition.charAt(i));
        }
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
                canvas.drawBitmap(
                    bmp[level[i*10 + j]],
                    null,
                    new Rect(j*width, i*height,(j+1)*width, (i+1)*height),
                    null
                );
            }
        }
    }

    protected boolean move(int offset) {
        Integer currentLocation = null;

        // find cat current location
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                if (level[i*10 + j] == HERO) {
                    currentLocation = i*10 + j;
                    Log.d("catch", String.valueOf(currentLocation));
                }
            }
        }

        // check if cat exists in area
        if (currentLocation == null) {
            return false;
        }

        // prepare destination array index
        int destination = currentLocation + offset;
        int boxLocation = currentLocation + offset * 2;

        // check if destination is in area range
        if (destination < 0 || destination >= 10 * 10) {
            return false;
        }

        // do not step left from left edge
        if (offset == -1 && currentLocation % 10 == 0) {
            return false;
        }

        // do not step right from right edge
        if (offset == 1 && currentLocation % 10 == 9) {
            return false;
        }

        // do not step on the wall
        if (level[destination] == WALL) {
            return false;
        }

        // if in destination is box or green box then move it
        if (level[destination] == BOX || level[destination] == BOXOK) {
            // check if box destination is in area range
            if (boxLocation < 0 || boxLocation >= 10 * 10) {
                return false;
            }

            // do not move box on the wall or another box
            if (level[boxLocation] == WALL || level[boxLocation] == BOX) {
                return false;
            }

            // if destination is goal then set green box
            if (level[boxLocation] == GOAL) {
                level[boxLocation] = BOXOK;
            } else {
                level[boxLocation] = BOX;
            }
        }

        // restore block on old cat location
        if (standingOnGoal) {
            level[currentLocation] = GOAL;
            standingOnGoal = false;
        } else {
            level[currentLocation] = EMPTY;
        }

        // if in destination is box or green box then set cat standing on goal
        if (level[destination] == GOAL || level[destination] == BOXOK) {
            standingOnGoal = true;
        }

        // set new cat location
        level[destination] = HERO;

        // redraw view
        invalidate();

        // play 440 Hz sound
        MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);
        mp.start();

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