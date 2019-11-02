package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.rigobobo.Model.Voto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VotoHelper {

    private static final String TABLE_NAME = "voto";
    private static final String ID = "ID";
    private static final String NOME = "nome";
    private static final String VOTO = "voto";
    private static final String LODE = "lode";
    private static final String DATA = "data";
    private static final String CREDITI = "crediti";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID        +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOME      +" TEXT, "
            + VOTO      +" INTEGER, "
            + LODE      +" BOOLEAN, "
            + DATA      +" INTEGER, "
            + CREDITI   +" INTEGER)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public VotoHelper(){}

    public void addVoto(Voto voto){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME, voto.getNome());
        contentValues.put(VOTO, voto.getVotoI());
        contentValues.put(LODE, voto.getLode());
        contentValues.put(DATA, voto.getData().getTime());
        contentValues.put(CREDITI, voto.getCrediti());
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public void addVoti(List<Voto> voti){
        for(Voto voto: voti){
            addVoto(voto);
        }
    }

    public void flushTable(){
        DBOpenHelper.getInstance().getWritableDatabase().delete(TABLE_NAME, null, null);
    }

    public List<Voto> getAll(String orderBy) {
        List<Voto> voti = new ArrayList<>();
        Cursor cursor = DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            Date data = new Date();
            data.setTime(cursor.getLong(cursor.getColumnIndex(DATA)));
            Voto voto = new Voto(
                    cursor.getString(cursor.getColumnIndex(NOME)),
                    cursor.getInt(cursor.getColumnIndex(VOTO)),
                    cursor.getInt(cursor.getColumnIndex(LODE))>0,
                    data,
                    cursor.getInt(cursor.getColumnIndex(CREDITI))
            );
            voti.add(voto);
        }
        cursor.close();
        return voti;
    }

    public List<Voto> getAll() {
        return getAll(ID + " ASC");
    }
}
