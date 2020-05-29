package com.example.appthoitiet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.appthoitiet.entities.User;

public class UserDB  {
    SQLiteDatabase database;
    DBHelper dbHelper;
    public UserDB(Context context) {
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
    public Cursor layUser(String email) {
        String[] cot = {
                DBHelper.COT_ID,
                DBHelper.COT_EMAIL,
                DBHelper.COT_PASSWORD,
        };
        String[] emailQuery = { email };
        Cursor cursor = null;
        cursor = database.rawQuery("SELECT * FROM User WHERE TRIM(_email) = '"+email.trim()+"'", null);
        return cursor;
    }
    public long them(User user) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COT_EMAIL, user.getEmail());
        values.put(DBHelper.COT_PASSWORD, user.getPassword());
        return database.insert(DBHelper.TEN_BANG_USER, null, values);
    }
}
