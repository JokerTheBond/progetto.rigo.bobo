package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.rigobobo.Model.Info;

import java.util.Date;

public class InfoHelper {

    private static final String TABLE_NAME = "info";
    private static final String ID = "ID";
    private static final String NOME = "nome";
    private static final String CORSO_DI_STUDIO = "corsoDiStudio";
    private static final String ANNO = "anno";
    private static final String FOTO_PROFILO = "fotoProfilo";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + ID                +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOME              +" TEXT, "
            + CORSO_DI_STUDIO   +" TEXT, "
            + ANNO              +" INT, "
            + FOTO_PROFILO      +" TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public InfoHelper(){}

    public void setInfo(Info info){
        flushTable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME, info.getNome());
        contentValues.put(CORSO_DI_STUDIO, info.getCorsoDiStudio());
        contentValues.put(ANNO, info.getAnno());
        contentValues.put(FOTO_PROFILO, info.getFotoProfilo());
        DBOpenHelper.getInstance().getWritableDatabase().insert(
                TABLE_NAME,  null, contentValues);
    }

    public void flushTable(){
        DBOpenHelper.getInstance().getWritableDatabase().delete(TABLE_NAME, null, null);
    }

    public Info getInfo() {
        Cursor cursor = DBOpenHelper.getInstance().getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, null);
        if( !cursor.moveToNext() ) return null;
        Info info = new Info(
                cursor.getString(cursor.getColumnIndex(NOME)),
                cursor.getString(cursor.getColumnIndex(CORSO_DI_STUDIO)),
                cursor.getInt(cursor.getColumnIndex(ANNO)),
                cursor.getString(cursor.getColumnIndex(FOTO_PROFILO))
        );
        cursor.close();
        return info;
    }
}
