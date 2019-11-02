package com.example.rigobobo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.rigobobo.View.MainActivity;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "RIGOBOBO";
    private static final int DB_VERSION = 7;

    private static final DBOpenHelper dbOpenHelper = new DBOpenHelper();

    private DBOpenHelper(){
        super(MainActivity.getContext(), DB_NAME, null, DB_VERSION);
    }

    public static DBOpenHelper getInstance(){
        return dbOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventHelper.CREATE_TABLE);
        db.execSQL(NotificaHelper.CREATE_TABLE);
        db.execSQL(VotoHelper.CREATE_TABLE);
        db.execSQL(TassaHelper.CREATE_TABLE);
        db.execSQL(PrenotazioneHelper.CREATE_TABLE);
        db.execSQL(AppelloHelper.CREATE_TABLE);
        db.execSQL(InfoHelper.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EventHelper.DROP_TABLE);
        db.execSQL(NotificaHelper.DROP_TABLE);
        db.execSQL(VotoHelper.DROP_TABLE);
        db.execSQL(TassaHelper.DROP_TABLE);
        db.execSQL(PrenotazioneHelper.DROP_TABLE);
        db.execSQL(AppelloHelper.DROP_TABLE);
        db.execSQL(InfoHelper.DROP_TABLE);
        onCreate(db);
    }

}
