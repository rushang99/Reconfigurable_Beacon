package com.example.rohit.driverapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import java.util.List;

public class BeaconAdapter extends ArrayAdapter<ibeaconDevice> {
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        ibeaconDevice beacon=getItem(position);
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);
            ((TextView)(view.findViewById(R.id.b_name))).setText(beacon.name);
            ((TextView)(view.findViewById(R.id.b_power))).setText("RSSI  "+beacon.power +" dbm");

        }
        else{
            view=convertView;
            ((TextView)(view.findViewById(R.id.b_name))).setText(beacon.name);
            ((TextView)(view.findViewById(R.id.b_power))).setText("RSSI  "+beacon.power+" dbm");
        }

        return view;
    }

    public BeaconAdapter(@NonNull Context context, int resource, @NonNull List<ibeaconDevice> objects) {
        super(context, resource, objects);
    }
}
