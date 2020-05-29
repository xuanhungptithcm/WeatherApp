package com.example.appthoitiet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.appthoitiet.entities.Weather;

public class WeatherItemDB {
    SQLiteDatabase database;
    DBHelper dbHelper;

    public WeatherItemDB(Context context) {
        dbHelper = new DBHelper(context);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        }
    }
    public void close() {
        dbHelper.close();
    }
    public Cursor layTatCaDuLieu() {
        String[] cot = {
                DBHelper.COT_ID,
                DBHelper.COT_DAY,
                DBHelper.COT_TIME,
                DBHelper.COT_NHIET_DO_CAO_NHAT,
                DBHelper.COT_NHIET_DO_THAP_NHAT,
                DBHelper.COT_NHIET_DO_TRUNG_BINH,
                DBHelper.COT_COLOR,
                DBHelper.COT_IMAGE,
        };

        Cursor cursor = null;
        cursor = database.query(DBHelper.TEN_BANG_WEATHER_ITEM, cot, null,null,null,null, null);
        return cursor;
    }
    public long them(Weather hd) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COT_DAY, hd.getDay());
        values.put(DBHelper.COT_TIME, hd.getTime());
        values.put(DBHelper.COT_NHIET_DO_CAO_NHAT, hd.getNhietDoCaoNhat());
        values.put(DBHelper.COT_NHIET_DO_THAP_NHAT, hd.getNhietDoThapNhat());
        values.put(DBHelper.COT_NHIET_DO_TRUNG_BINH, hd.getNhietDoTrungBinh());
        values.put(DBHelper.COT_COLOR, hd.getColor());
        values.put(DBHelper.COT_IMAGE, hd.getImage());
        return database.insert(DBHelper.TEN_BANG_WEATHER_ITEM, null, values);

    }

    public long sua(Weather hd) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COT_DAY, hd.getDay());
        values.put(DBHelper.COT_TIME, hd.getTime());
        values.put(DBHelper.COT_NHIET_DO_CAO_NHAT, hd.getNhietDoCaoNhat());
        values.put(DBHelper.COT_NHIET_DO_THAP_NHAT, hd.getNhietDoThapNhat());
        values.put(DBHelper.COT_NHIET_DO_TRUNG_BINH, hd.getNhietDoTrungBinh());
        values.put(DBHelper.COT_COLOR, hd.getColor());
        values.put(DBHelper.COT_IMAGE, hd.getImage());
        return database.update(DBHelper.TEN_BANG_WEATHER_ITEM, values, DBHelper.COT_ID + " = " + hd.getId(), null);
    }
}
