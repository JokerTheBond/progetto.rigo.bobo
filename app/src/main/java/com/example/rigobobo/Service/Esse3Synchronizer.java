package com.example.rigobobo.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.rigobobo.DataManager.AppelloManager;
import com.example.rigobobo.DataManager.InfoManager;
import com.example.rigobobo.DataManager.NotificaManager;
import com.example.rigobobo.DataManager.PrenotazioneManager;
import com.example.rigobobo.DataManager.TassaManager;
import com.example.rigobobo.DataManager.VotoManager;
import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.Model.Notifica;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.Model.Voto;
import com.example.rigobobo.R;
import com.example.rigobobo.View.MainActivity;
import com.example.rigobobo.View.TassaActivity;
import com.example.rigobobo.View.VotoActivity;

import java.util.Date;
import java.util.List;

public class Esse3Synchronizer extends Worker {

    public static final String TAG = "Esse3Synchronizer";
    private static final String CHANNEL_ID = "Esse3Channel";

    //Settable parameters for notify
    private String title = "Test";
    private String description = "Test";
    private Class activity = MainActivity.class;

    public Esse3Synchronizer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "Work in progress");

        List<Voto> voti1 = VotoManager.getInstance().getVotiData(false);
        List<Voto> voti2 = VotoManager.getInstance().getVotiData(true);
        if( !voti2.equals(voti1) ){
            if( voti2.size() > voti1.size() ){
                this.title = "Disponibili nuovi voti";
                this.description = "Hai ricevuto nuovi voti, vieni a scoprirli";
                this.activity = VotoActivity.class;
                Notifica notifica = new Notifica(this.title, this.description, Notifica.TIPO_VOTO, (new Date()));
                NotificaManager.getInstance().addNotifica(notifica);
                sendNotifica();
            }
        }

        List<Tassa> tasse1 = TassaManager.getInstance().getTasseData(false);
        List<Tassa> tasse2 = TassaManager.getInstance().getTasseData(true);
        if( !tasse2.equals(tasse1) ){
            if( tasse2.size() > tasse1.size() ){
                this.title = "ATTENZIONE: nuove tasse";
                this.description = "E' stata aggiunta una nuova tassa da pagare";
                this.activity = TassaActivity.class;
                Notifica notifica = new Notifica(this.title, this.description, Notifica.TIPO_TASSA, (new Date()));
                NotificaManager.getInstance().addNotifica(notifica);
                sendNotifica();
            }
        }

        AppelloManager.getInstance().getAppelliData(true);
        PrenotazioneManager.getInstance().getPrenotazioniData(true);
        InfoManager.getInstance().getInfoData(true);

        return Worker.Result.success();
    }

    private void sendNotifica(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        Intent resultIntent = new Intent(MainActivity.getContext(), this.activity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.getContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(MainActivity.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_home)
                .setContentTitle(this.title)
                .setContentText(this.description)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //TODO Fixare ntofica per android 8+
            CharSequence name = "RigoBoboC";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationManager notificationManager = MainActivity.getContext().getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(channel);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.getContext());
            notificationManagerCompat.notify(0,notification);
        }
        else {
            // notificationId is a unique int for each notification that you must define
            NotificationManager notificationManager = MainActivity.getContext().getSystemService(NotificationManager.class);
            notificationManager.notify(0, notification);
        }
    }
}
