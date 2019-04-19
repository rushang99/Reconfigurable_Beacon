package com.example.rohit.driverapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DatabaseHelper2 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "location.db";
    public static final String TABLE_NAME = "gps_location";
    public static final String LOCATION = "LOCATION";
    public static final String BEACON_NAME = "BEACON_NAME";


    public DatabaseHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (BEACON_NAME TEXT, LOCATION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: " + sqLiteDatabase.toString() + sqLiteDatabase.getMaximumSize());
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d(TAG, "onUpgrade: " + sqLiteDatabase.toString() + sqLiteDatabase.getMaximumSize());
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String beacon_name, String location1) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BEACON_NAME, beacon_name);
        contentValues.put(LOCATION, location1);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "insertData: " + result);
        sqLiteDatabase.close();

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor get_cood(String name)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from gps_location where BEACON_NAME='"+name+"'",null);
        return cursor;
    }

    public void update_loc(String name,String location)
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from gps_location where BEACON_NAME='"+name+"'",null);
        if(cursor.getCount()<=0)
        {
            this.insertData(name,location);
        }
        else
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BEACON_NAME, name);
            contentValues.put(LOCATION, location);
            sqLiteDatabase.update(TABLE_NAME, contentValues, "BEACON_NAME = ?", new String[]{name});
        }
    }


}