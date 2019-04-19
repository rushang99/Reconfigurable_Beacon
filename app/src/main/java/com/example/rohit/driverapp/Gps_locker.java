package com.example.rohit.driverapp;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Gps_locker extends AppCompatActivity {

    BluetoothDevice device;
    String device_name;
    public ArrayList<String> gps = new ArrayList<>();
    DatabaseHelper2 db = HomeScreen.gps_locations;
    public Button gps_button11;
    private Button maps_butt;
    private TextView beacon;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent i= getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_locker);

        gps_button11 = (Button) findViewById(R.id.gps_button1);
        maps_butt=(Button) findViewById(R.id.maps_button);
        beacon = (TextView) findViewById(R.id.Beacon_name);
        //device=i.getParcelableExtra("DEVICE");


            gps_button11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    device_name = beacon.getText().toString();
                    Cursor information = db.get_cood(device_name);
                    if (information.getCount() > 0) {
                        if (information.moveToFirst()) {
                            gps.clear();
                            gps.add("The last location where the Beacon was scanned is: ");
                            gps.add(information.getString(1));
                        }

                        final ListView configurations = (android.widget.ListView) findViewById(R.id.gps_list);

                        ArrayAdapter<String> infos;
                        infos = new ArrayAdapter<String>(Gps_locker.this, android.R.layout.simple_list_item_1, gps);
                        configurations.setAdapter(infos);
                    }

                }


            });

        maps_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                device_name = beacon.getText().toString();
                Cursor information = db.get_cood(device_name);
                if (information.getCount() > 0) {
                    if (information.moveToFirst()) {
                        Log.d("hello",information.getString(1));
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+information.getString(1)+"(Last location of your beacon)");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                }

//                Log.d("hello",information.getString(1));
//                Uri gmmIntentUri = Uri.parse("geo:"+information.getString(1));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(mapIntent);
//                }

            }


        });





    }
}
