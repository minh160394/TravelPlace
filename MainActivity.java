package com.finalproject21.chooseyourfavoriteplacetotravel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView addPlaces;
    static ArrayList<String> addedLocations = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static LayoutActivity arrayAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addPlaces = findViewById(R.id.addPlaces);
        if(addedLocations.isEmpty()) {
            addedLocations.add("Add new favorite traveling places");
            locations.add(new LatLng(0, 0));
        }
        arrayAdapter = new LayoutActivity(this,R.layout.activity_layout,addedLocations);
        addPlaces.setAdapter(arrayAdapter);
    }
}
