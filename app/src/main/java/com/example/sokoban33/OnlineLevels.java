package com.example.sokoban33;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlineLevels {
    public static List<String[]> resultList = new ArrayList<String[]>();

    public List<String[]> download() {
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL("http://marekhanus.cz/tamz_2.txt");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String str;
                    while ((str = in.readLine()) != null) {
                        String[] row = str.split(";");
                        resultList.add(row);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("OnlineLevelsERR", e.toString());
                }

                Log.d("OnlineLevelsOK", resultList.get(0)[1]);
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            Log.d("OnlineLevelsERR", e.toString());
        }

        return resultList;
    }
}
