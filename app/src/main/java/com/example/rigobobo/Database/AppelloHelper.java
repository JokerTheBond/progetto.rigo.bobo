package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;


import com.example.rigobobo.Model.Appello;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppelloHelper {

    private static final String TABLE_NAME = "appello";
    private static final String ID = "ID";
    private static final String NOME = "nome";
    private static final String DESC = "desc";
    private static final String DATA = "data";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID    +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOME  +" TEXT, "
            + DESC  +" TEXT, "
            + DATA  +" INT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public AppelloHelper(){}

    public void addAppello(Appello appello){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME, appello.getNome());
        contentValues.put(DATA, appello.getData().getTime());
        contentValues.put(DESC, appello.getDesc());
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public void addAppelli(List<Appello> appelli){
        for(Appello appello: appelli){
            addAppello(appello);
        }
    }

    public void flushTable(){
        DBOpenHelper.getInstance().getWritableDatabase().delete(TABLE_NAME, null, null);
    }

    public List<Appello> getAll(String orderBy) {
        List<Appello> appelli = new ArrayList<>();
        Cursor cursor = DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            Date data = new Date();
            data.setTime(cursor.getLong(cursor.getColumnIndex(DATA)));
            Appello appello = new Appello(
                    cursor.getString(cursor.getColumnIndex(NOME)),
                    data,
                    cursor.getString(cursor.getColumnIndex(DESC))
            );
            appelli.add(appello);
        }
        cursor.close();
        return appelli;
    }

    public List<Appello> getAll() {
        return getAll(ID + " ASC");
    }
}
