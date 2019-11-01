package com.example.rigobobo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.rigobobo.View.MainActivity;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "RIGOBOBO";
    private static final int DB_VERSION = 4;

    private static final DBOpenHelper dbOpenHelper = new DBOpenHelper();

    private DBOpenHelper(){//@Nullable Context context) {
        super(MainActivity.getContext(), DB_NAME, null, DB_VERSION);
    }

    public static DBOpenHelper getInstance(){
        return dbOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventHelper.CREATE_TABLE);
        db.execSQL(NotificaHelper.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EventHelper.DROP_TABLE);
        db.execSQL(NotificaHelper.DROP_TABLE);
        onCreate(db);
    }

}
