package com.example.sokoban33;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.provider.BaseColumns;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    private boolean ENABLE_SOUNDS = true;

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

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LEVEL,
        };

        // Filter results WHERE "title" = 'My Title'
//        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_LEVEL + " = ?";
//        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_LEVEL + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List itemIds = new ArrayList<>();
        Log.d("LEVEL_STATE_SAVED_COUNT", String.valueOf(cursor.getCount()));
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_LEVEL));
            itemIds.add(itemId);
            String levelState = cursor.getString(1);
            Log.d("LEVEL_STATE_SAVED", levelState);
            redrawLevel(levelState);
            return;
        }
        cursor.close();

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
    }

    void redrawLevel(String levelDefinition) {
        for (int i = 0; i < levelDefinition.length(); i++) {
            level[i] = Character.getNumericValue(levelDefinition.charAt(i));
        }
    }

    public void setSound(boolean sound) {
        ENABLE_SOUNDS = sound;
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

    public String myJoin(int[] arr, String separator) {
        if (null == arr || 0 == arr.length) return "";

        StringBuilder sb = new StringBuilder(256);
        sb.append(arr[0]);

        //if (arr.length == 1) return sb.toString();

        for (int i = 1; i < arr.length; i++) sb.append(separator).append(arr[i]);

        return sb.toString();
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

                if (ENABLE_SOUNDS) {
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);
                    mp.start();
                }
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

        // play 440 Hz sound if enabled
        if (ENABLE_SOUNDS) {
            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.ding);
            mp.start();
        }

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_LEVEL + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "MyTitle" };
        // Issue SQL statement.
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LEVEL, myJoin(level, ""));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        Log.d("LEVEL_STATE", myJoin(level, ""));

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