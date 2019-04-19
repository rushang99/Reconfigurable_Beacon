package com.example.rohit.driverapp;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Information extends AppCompatActivity {

    BluetoothDevice device;
    String device_name;
    public ArrayList<String> info=new ArrayList<>();
    private TextView head;
    private TextView tail;

    DatabaseHelper db = HomeScreen.device_db;

    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        Intent i= getIntent();
        device=i.getParcelableExtra("DEVICE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        head=(TextView) findViewById(R.id.textView4);
        tail=(TextView) findViewById(R.id.textView5);

        device_name=device.getName();
        //info.add(device_name);
        Cursor information=db.get_name(device_name);
        if(information.moveToFirst())
        {
//            \

            if(information.getString(0).equals("Beacon 0"))
            {   head.setText("Hey there! You have selected the beacon located at Assistech Lab IIT Delhi");
                //info.add(Integer.toString(information.getInt(1)));
                //info.add(Integer.toString(information.getInt(2)));
                //info.add(information.getString(3));
                tail.setText("ASSISTECH is an inter-disciplinary group of faculty, research staff and students, which is engaged in using modern technology for finding affordable solutions for the visually impaired. The focus is on mobility and education which we consider are fundamental to enable any person to live independently and with dignity. Apart from working on specific solutions based on needs identified by the users themselves, we hope to create an eco-system that helps sensitize and ignite a number of young minds towards real day-to-day challenges of the differently abled");
            }
            if(information.getString(0).equals("Beacon 1"))
            {   head.setText("Hey there! You have selected the beacon located at the Toilet");
                //info.add(Integer.toString(information.getInt(1)));
                //info.add(Integer.toString(information.getInt(2)));
                //info.add(information.getString(3));
                tail.setText("The toilet is located on the backside of the Seminar Room");
            }
            if(information.getString(0).equals("Beacon 2"))
            {   head.setText("Hey there! You have selected the beacon located at the Lift");
                //info.add(Integer.toString(information.getInt(1)));
                //info.add(Integer.toString(information.getInt(2)));
                //info.add(information.getString(3));
                tail.setText("The lift is located between the Seminar Room and the Assistech Lab. It can be used to commute between any of the 4 floors of the SIT building");
            }
            if(information.getString(0).equals("Beacon 3"))
            {   head.setText("Hey there! You have selected the beacon located at the Seminar Room");
                //info.add(Integer.toString(information.getInt(1)));
                //info.add(Integer.toString(information.getInt(2)));
                //info.add(information.getString(3));
                tail.setText("The seminar room is the most sought after place for project demonstrations and presentations");
            }

        }


//        final ListView configurations=(ListView) findViewById(R.id.info_list);
//
//        ArrayAdapter<String> infos=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,info);
//        configurations.setAdapter(infos);
    }
}
