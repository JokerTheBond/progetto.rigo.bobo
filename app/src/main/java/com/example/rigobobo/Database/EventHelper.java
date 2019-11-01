package com.example.rigobobo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import androidx.annotation.Nullable;

public class EventHelper {

    public static final String EVENT_TABLE_NAME = "eventstable";
    public static final String ID = "ID";
    public static final String EVENT = "event";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String Notify = "notify";

    public static final String CREATE_TABLE = "create table "+EVENT_TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +EVENT+" TEXT, "
            +TIME+ " TEXT, "+DATE+ " TEXT, "+MONTH+ " TEXT, "+YEAR+ " TEXT, "+Notify+" TEXT)";
    public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+EVENT_TABLE_NAME;

    public EventHelper(){}
    public EventHelper(@Nullable Context context) { }

    public void SaveEvent(String event,String time,String date,String month,String year,String notify){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT,event);
        contentValues.put(TIME,time);
        contentValues.put(DATE,date);
        contentValues.put(MONTH,month);
        contentValues.put(YEAR,year);
        contentValues.put(Notify,notify);
        DBOpenHelper.getInstance().getReadableDatabase().insert(
                EVENT_TABLE_NAME, null, contentValues);
    }

    public Cursor getAll(){
        return DBOpenHelper.getInstance().getReadableDatabase().query(EVENT_TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor ReadEvents(String date,SQLiteDatabase database){
        String [] Projections = {EVENT,TIME,DATE,MONTH,YEAR};
        String Selection = DATE +"=?";
        String [] SelectionArgs= {date};

        return database.query(EVENT_TABLE_NAME, Projections, Selection,SelectionArgs, null, null, null);
    }

    public Cursor ReadIDEvents(String date, String event, String time, SQLiteDatabase database){
        String [] Projections = {ID,Notify};
        String Selection = DATE +"=? and "+EVENT+"=? and "+TIME+"=?";
        String [] SelectionArgs= {date,event,time};

        return database.query(EVENT_TABLE_NAME, Projections, Selection,SelectionArgs, null, null, null);
    }

    public Cursor ReadEventsMonth(String month, String year,SQLiteDatabase database){
        String [] Projections = {EVENT,TIME,DATE,MONTH,YEAR};
        String Selection = MONTH +"=? and "+YEAR +"=?";
        String [] SelectionArgs= {month,year};

        return database.query(EVENT_TABLE_NAME, Projections, Selection,SelectionArgs, null, null, null);
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database){

        String selection = EVENT+"=? and "+DATE+"=? and "+TIME+"=?";
        String [] selectionArg = {event,date,time};
        database.delete(EVENT_TABLE_NAME,selection,selectionArg);
    }

    public void updateEvent(String date, String event, String time, String notify ,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Notify,notify);
        String Selection = DATE +"=? and "+EVENT+"=? and "+TIME+"=?";
        String [] SelectionArgs= {date,event,time};

        database.update(EVENT_TABLE_NAME, contentValues, Selection,SelectionArgs);
    }
}
