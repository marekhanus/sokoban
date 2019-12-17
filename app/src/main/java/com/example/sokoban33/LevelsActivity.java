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
    private boolean LOAD_ONLINE_LEVELS = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        final ArrayList<String> levelDefinition = new ArrayList<>();
        ArrayList<String> levelNames = new ArrayList<>();


        if (LOAD_ONLINE_LEVELS) {
            OnlineLevels onlineLevels = new OnlineLevels();
            List<String[]> data = onlineLevels.download();
            for (String[] row : data) {
                levelDefinition.add(row[0]);
                levelNames.add(row[1]);
            }
        } else {
            InputStream inputStream = getBaseContext().getResources().openRawResource(R.raw.level);
            CSVFile csvFile = new CSVFile(inputStream);
            List<String[]> data = csvFile.read();
            for (String[] row : data) {
                levelDefinition.add(row[0]);
                levelNames.add(row[1]);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_levels, levelNames);

        ListView listView = findViewById(R.id.levels_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                myIntent.putExtra("LEVEL_DEFINITION", levelDefinition.get(i));
                startActivity(myIntent);
            }
        });
    }
}
