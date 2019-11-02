package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.rigobobo.Model.Tassa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TassaHelper {

    private static final String TABLE_NAME = "tassa";
    private static final String ID = "ID";
    private static final String IMPORTO = "importo";
    private static final String SCADENZA = "scadenza";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID        +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IMPORTO   +" FLOAT, "
            + SCADENZA  +" INT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public TassaHelper(){}

    public void addTassa(Tassa tassa){
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMPORTO, tassa.getImporto());
        contentValues.put(SCADENZA, tassa.getScadenza().getTime());
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public void addTasse(List<Tassa> tasse){
        for(Tassa tassa: tasse){
            addTassa(tassa);
        }
    }

    public void flushTable(){
        DBOpenHelper.getInstance().getWritableDatabase().delete(TABLE_NAME, null, null);
    }

    public List<Tassa> getAll(String orderBy) {
        List<Tassa> tasse = new ArrayList<>();
        Cursor cursor = DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            Date scadenza = new Date();
            scadenza.setTime(cursor.getLong(cursor.getColumnIndex(SCADENZA)));
            Tassa tassa = new Tassa(
                    cursor.getFloat(cursor.getColumnIndex(IMPORTO)),
                    scadenza
            );
            tasse.add(tassa);
        }
        cursor.close();
        return tasse;
    }

    public List<Tassa> getAll() {
        return getAll(ID + " ASC");
    }
}
