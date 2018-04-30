package com.finalproject21.chooseyourfavoriteplacetotravel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LayoutActivity extends ArrayAdapter<String> {
    int resourcei;
    private Context contextm;

   public LayoutActivity(Context context,int resource, ArrayList<String> records)
   {
       super(context, resource , records);
       contextm = context;
       resourcei = resource;
   }

   public View getView (final int position, View convertView, ViewGroup parent){
       final String items = getItem(position);
       final int i = position;
        LayoutInflater inflater = LayoutInflater.from(getContext());
       convertView = inflater.inflate(resourcei,parent,false);
       TextView list_Text = (TextView)convertView.findViewById(R.id.ListText);
       Button button = (Button)convertView.findViewById(R.id.ListButton);
       if(position == 0){
           button.setVisibility(View.INVISIBLE);
       }
       list_Text.setText(items);
       list_Text.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               Intent intent = new Intent(contextm,MapsActivity.class);
               intent.putExtra("locationId", i);
               contextm.startActivity(intent);
           }

       });

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                MainActivity.addedLocations.remove(position);
                MainActivity.locations.remove(position);
                MainActivity.arrayAdapter.notifyDataSetChanged();
           }
       });


       return  convertView;
   }

}
