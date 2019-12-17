package com.example.sokoban33;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlineLevels {
    public static List<String> response;

    public List<String> download() {
        new Thread(new Runnable(){
            public void run(){
                final List<String> urls = new ArrayList<String>();

                try {
                    URL url = new URL("http://marekhanus.cz/tamz_2.txt");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String str;
                    while ((str = in.readLine()) != null) {
                        urls.add(str);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("OnlineLevels", e.toString());
                }

                response = urls;
            }
        }).start();

        return response;
    }
}
