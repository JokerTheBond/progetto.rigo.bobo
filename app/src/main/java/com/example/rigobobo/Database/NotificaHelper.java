package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.rigobobo.Model.Notifica;
import com.example.rigobobo.Model.Voto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void addNotifica(Notifica notifica){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIPO, notifica.getTipo());
        contentValues.put(TITOLO, notifica.getTitolo());
        contentValues.put(DESCRIZIONE, notifica.getDescrizione());
        contentValues.put(DATA_ORA, notifica.getDataOra().getTime());
        contentValues.put(VISTA, notifica.getVista());
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public void flushTable(){
        DBOpenHelper.getInstance().getWritableDatabase().delete(TABLE_NAME, null, null);
    }


    public List<Notifica> getAll(String orderBy) {
        List<Notifica> notifiche = new ArrayList<>();
        Cursor cursor = DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            Date data = new Date();
            data.setTime(cursor.getLong(cursor.getColumnIndex(DATA_ORA)));
            Notifica notifica = new Notifica(
                    cursor.getString(cursor.getColumnIndex(TITOLO)),
                    cursor.getString(cursor.getColumnIndex(DESCRIZIONE)),
                    cursor.getString(cursor.getColumnIndex(TIPO)),
                    data,
                    cursor.getInt(cursor.getColumnIndex(VISTA))>0
            );
            notifiche.add(notifica);
        }
        cursor.close();
        return notifiche;
    }

    public List<Notifica> getAll() {
        return getAll(ID + " DESC");
    }
}
