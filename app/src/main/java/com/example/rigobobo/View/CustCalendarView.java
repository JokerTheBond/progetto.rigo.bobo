package com.example.rigobobo.View;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rigobobo.Database.EventHelper;
import com.example.rigobobo.Model.Events;
import com.example.rigobobo.R;
import com.example.rigobobo.Service.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.example.rigobobo.Database.EventHelper.DATE;
import static com.example.rigobobo.Database.EventHelper.EVENT;
import static com.example.rigobobo.Database.EventHelper.MONTH;
import static com.example.rigobobo.Database.EventHelper.TIME;
import static com.example.rigobobo.Database.EventHelper.YEAR;

public class CustCalendarView extends LinearLayout{
    ImageButton Nextbtn,Previousbtn;
    TextView CurrentDate;
    GridView gridview;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat= new SimpleDateFormat( "MMMM yyyy",Locale.ITALIAN);
    SimpleDateFormat monthFormat = new SimpleDateFormat( "MMMM",Locale.ITALIAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat( "yyyy",Locale.ITALIAN);
    SimpleDateFormat eventDateFormate = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);

    MyGridAdapter myGridAdapter;

    AlertDialog alertDialog;

    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    int alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute;

    EventHelper eventHelper;

    public CustCalendarView(Context context) {
        super(context);
    }

    public CustCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        Previousbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        Nextbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event_layout, null);
                final EditText EventName = addView.findViewById(R.id.eventname);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                ImageButton SetTime = addView.findViewById(R.id.seteventtime);
                final CheckBox alarmMe = addView.findViewById(R.id.aLarmme);
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(dates.get(position));
                alarmYear = dateCalendar.get(Calendar.YEAR);
                alarmMonth = dateCalendar.get(Calendar.MONTH);
                alarmDay = dateCalendar.get(Calendar.DAY_OF_MONTH);

                Button AddEvent = addView.findViewById(R.id.addevent);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minuts = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                        c.set(Calendar.MINUTE,minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformate= new SimpleDateFormat( "K:mm a",Locale.ITALIAN);
                                        String event_Time = hformate.format(c.getTime());
                                        EventTime.setText(event_Time);
                                        alarmHour = c.get(Calendar.HOUR_OF_DAY);
                                        alarmMinute = c.get(Calendar.MINUTE);
                                    }
                                },hours,minuts,false);
                        timePickerDialog.show();
                    }
                });
                final String date = eventDateFormate.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(alarmMe.isChecked()){
                            SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date,month,year,"on");
                            SetUpCalendar();
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute);
                            setAlarm(calendar,EventName.getText().toString(),EventTime.getText().toString(),getRequestCode(date,EventName.getText().toString(),EventTime.getText().toString()));
                            alertDialog.dismiss();

                        }else {
                            SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date,month,year,"off");
                            SetUpCalendar();
                            alertDialog.dismiss();
                        }
                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });




        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormate.format(dates.get(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout,null);

                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(),CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });

                return true;
            }
        });
    }

    private int getRequestCode(String date, String event, String time){
        int code =0;
        eventHelper = new EventHelper();
        //SQLiteDatabase database = eventHelper.getReadableDatabase();
        Cursor cursor = eventHelper.ReadIDEvents(date,event,time);
        while (cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(EventHelper.ID));
        }
        cursor.close();
        //eventHelper.close();

        return code;

    }

    private void setAlarm(Calendar calendar, String event, String time, int RequestCOde){
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event",event);
        intent.putExtra("time",time);
        intent.putExtra("id",RequestCOde);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCOde,intent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    private ArrayList<Events> CollectEventByDate (String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        eventHelper = new EventHelper();
        //SQLiteDatabase database = eventHelper.getReadableDatabase();
        Cursor cursor = eventHelper.ReadEvents(date);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(EVENT));
            String time = cursor.getString(cursor.getColumnIndex(TIME));
            String Date = cursor.getString(cursor.getColumnIndex(DATE));
            String month = cursor.getString(cursor.getColumnIndex(MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(YEAR));
            Events events = new Events(event,time,Date,month,Year);
            arrayList.add(events);
        }
        cursor.close();
        //eventHelper.close();

        return arrayList;
    }



    public CustCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent(String event, String time, String date, String month, String year, String notify){
        eventHelper = new EventHelper();
        //SQLiteDatabase database = eventHelper.getWritableDatabase();
        eventHelper.SaveEvent(event,time,date,month,year,notify);
        //eventHelper.close();
        Toast.makeText(context, "Evento Salvato", Toast.LENGTH_SHORT).show();

    }

    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        Nextbtn = view.findViewById(R.id.Next);
        Previousbtn=view.findViewById(R.id.Previous);
        CurrentDate=view.findViewById(R.id.current_Date);
        gridview=view.findViewById(R.id.Grid);
    }

    private void SetUpCalendar(){
        String cDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(cDate);
        dates.clear();
        Calendar monthCalendar= (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS){

            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList);
        gridview.setAdapter(myGridAdapter);
    }

    private void CollectEventsPerMonth(String Month, String year){
        eventsList.clear();
        eventHelper= new EventHelper();
        //SQLiteDatabase database =eventHelper.getReadableDatabase();
        Cursor cursor =eventHelper.ReadEventsMonth(Month,year);
        while(cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(EVENT));
            String time = cursor.getString(cursor.getColumnIndex(TIME));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            String month = cursor.getString(cursor.getColumnIndex(MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(YEAR));
            Events events = new Events(event,time,date,month,Year);
            eventsList.add(events);
        }
        cursor.close();
        //eventHelper.close();
    }
}
