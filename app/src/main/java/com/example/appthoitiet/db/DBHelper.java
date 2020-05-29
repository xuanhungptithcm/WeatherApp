package com.example.appthoitiet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {

    public static  final String TEN_DATABASE = "WeatherApp";
    public static final String TEN_BANG_WEATHER_ITEM= "WeatherItem";

    public static final  String COT_ID = "_id";
    public static final  String COT_DAY = "_day";
    public static final  String COT_TIME = "_time";
    public static final  String COT_NHIET_DO_CAO_NHAT = "_nhietDoThapNhat";
    public static final  String COT_NHIET_DO_THAP_NHAT = "_nhietDoCaoNhat";
    public static final  String COT_NHIET_DO_TRUNG_BINH = "_nhietDoTrungBinh";
    public static final  String COT_COLOR = "_color";
    public static final  String COT_IMAGE = "_image";
    public static final  String TAO_BANG_WEATHER_ITEM = "create table " + TEN_BANG_WEATHER_ITEM + " ( "
            + COT_ID + " integer primary key autoincrement , "
            + COT_DAY + " text not null, "
            + COT_TIME + " text not null, "
            + COT_NHIET_DO_CAO_NHAT + " text not null, "
            + COT_NHIET_DO_THAP_NHAT + " text not null, "
            + COT_NHIET_DO_TRUNG_BINH + " text not null, "
            + COT_COLOR + " text not null, "
            + COT_IMAGE + " text not null );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TAO_BANG_WEATHER_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public DBHelper(Context context) {
        super(context,TEN_DATABASE,null,1);
    }
}
