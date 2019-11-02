package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import java.util.Date;

public class NotificaHelper {

    private static final String TABLE_NAME = "notifica";
    private static final String ID = "ID";
    private static final String TIPO = "tipo";
    private static final String TITOLO = "titolo";
    private static final String DESCRIZIONE = "descrizione";
    private static final String DATA_ORA = "dataora";
    private static final String VISTA = "vista";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID           +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TIPO         +" TEXT, "
            + TITOLO       +" TEXT, "
            + DESCRIZIONE  +" TEXT, "
            + DATA_ORA     +" INT, "
            + VISTA        +" BOOLEAN)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public NotificaHelper(){}

    public void addNotifica(String tipo, String titolo, String descrizione, Date data_ora){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIPO, tipo);
        contentValues.put(TITOLO, titolo);
        contentValues.put(DESCRIZIONE, descrizione);
        contentValues.put(DATA_ORA, data_ora.getTime());
        contentValues.put(VISTA, false);
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public Cursor getAll(){
        return DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, null);
    }
}
