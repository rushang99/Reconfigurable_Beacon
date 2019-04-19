package com.example.rohit.driverapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "devices.db";
    public static final String TABLE_NAME = "devices_data";
    public static final String BEACON_NAME = "BEACON_NAME";
    public static final String MAJOR_VAL = "MAJOR_VAL";
    public static final String MINOR_VAL = "MINOR_VAL";
    public static final String UUID = "UUID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (BEACON_NAME TEXT, MAJOR_VAL INTEGER , MINOR_VAL INTEGER, UUID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: " + sqLiteDatabase.toString() + sqLiteDatabase.getMaximumSize());
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d(TAG, "onUpgrade: " + sqLiteDatabase.toString() + sqLiteDatabase.getMaximumSize());
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String beacon_name, int major_val, int minor_val, String uuid) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BEACON_NAME, beacon_name);
        contentValues.put(MAJOR_VAL, major_val);
        contentValues.put(MINOR_VAL, minor_val);
        contentValues.put(UUID, uuid);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "insertData: " + result);
        sqLiteDatabase.close();

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor get_name(String name)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from devices_data where BEACON_NAME='"+name+"'",null);
        return cursor;
    }

}