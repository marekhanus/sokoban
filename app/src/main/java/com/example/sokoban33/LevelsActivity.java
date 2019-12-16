package com.example.sokoban33;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        ArrayList<String> arrayList = new ArrayList<>();

        InputStream inputStream = getBaseContext().getResources().openRawResource(R.raw.level);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> data = csvFile.read();
        for (String[] row : data) {
            arrayList.add(row[1]);
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_levels, arrayList);

        ListView listView = findViewById(R.id.levels_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
