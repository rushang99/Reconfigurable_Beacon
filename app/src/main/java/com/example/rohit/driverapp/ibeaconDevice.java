package com.example.rohit.driverapp;

import android.support.annotation.NonNull;

import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

public class ibeaconDevice implements   Comparable{
    String name;
    int power;
    int major;
    int minor;

    @Override
    public int compareTo(@NonNull Object o) {
        if(power>((ibeaconDevice)o).power){
            return 1;
        }
        if(power<((ibeaconDevice)o).power){
            return -1;
        }
        else
            return 0;
    }

    public ibeaconDevice(IBeacon beacon, String name, int power) {
        this.name=name;
        this.power=power;
        this.major=beacon.getMajor();
        this.minor=beacon.getMinor();

    }

}
