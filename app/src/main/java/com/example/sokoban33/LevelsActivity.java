package com.example.sokoban33;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Sample level");
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_levels, arrayList);

        ListView listView = findViewById(R.id.levels_list);
        listView.setAdapter(adapter);
    }
}
