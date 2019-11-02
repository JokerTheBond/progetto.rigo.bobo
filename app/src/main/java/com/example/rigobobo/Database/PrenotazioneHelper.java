package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.rigobobo.Model.Prenotazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrenotazioneHelper {

    private static final String TABLE_NAME = "prenotazione";
    private static final String ID = "ID";
    private static final String NOME = "nome";
    private static final String DESC = "desc";
    private static final String DATA = "data";
    private static final String EDIFICIO = "edificio";
    private static final String AULA = "aula";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID        +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOME      +" TEXT, "
            + DESC      +" TEXT, "
            + DATA      +" INT, "
            + EDIFICIO  +" TEXT, "
            + AULA      +" TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public PrenotazioneHelper(){}

    public void addPrenotazione(Prenotazione prenotazione){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME, prenotazione.getNome());
        contentValues.put(DESC, prenotazione.getDesc());
        contentValues.put(DATA, prenotazione.getData().getTime());
        contentValues.put(EDIFICIO, prenotazione.getEdificio());
        contentValues.put(AULA, prenotazione.getAula());
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public void addPrenotazioni(List<Prenotazione> prenotazioni){
        for(Prenotazione prenotazione: prenotazioni){
            addPrenotazione(prenotazione);
        }
    }

    public void flushTable(){
        DBOpenHelper.getInstance().getWritableDatabase().delete(TABLE_NAME, null, null);
    }

    public List<Prenotazione> getAll(String orderBy) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        Cursor cursor = DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            Date data = new Date();
            data.setTime(cursor.getLong(cursor.getColumnIndex(DATA)));
            Prenotazione prenotazione = new Prenotazione(
                    cursor.getString(cursor.getColumnIndex(NOME)),
                    cursor.getString(cursor.getColumnIndex(DESC)),
                    data,
                    cursor.getString(cursor.getColumnIndex(EDIFICIO)),
                    cursor.getString(cursor.getColumnIndex(AULA))
            );
            prenotazioni.add(prenotazione);
        }
        cursor.close();
        return prenotazioni;
    }

    public List<Prenotazione> getAll() {
        return getAll(ID + " ASC");
    }
}
