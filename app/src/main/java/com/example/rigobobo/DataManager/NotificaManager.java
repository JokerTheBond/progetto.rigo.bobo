package com.example.rigobobo.DataManager;

import androidx.annotation.NonNull;

import com.example.rigobobo.Database.NotificaHelper;
import com.example.rigobobo.Model.Notifica;

import java.util.List;

public class NotificaManager {

    private static NotificaManager notificheManager = new NotificaManager();
    private static NotificaHelper notificheHelper = new NotificaHelper();

    private NotificaManager (){ }
    public static NotificaManager getInstance(){ return notificheManager; }

    public void clear(){
        notificheHelper.flushTable();
    }

    public void addNotifica(Notifica notifica){
        notificheHelper.addNotifica(notifica);
    }

    public void setAllSeen(){}

    public List<Notifica> getNotifiche(){
        return notificheHelper.getAll();
    }

    public int countUnseen(){ return 0; }
}
