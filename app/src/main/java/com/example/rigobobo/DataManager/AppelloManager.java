package com.example.rigobobo.DataManager;

import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppelloManager {

    public static int APPELLO_ORDER_BY_OLDEST = 0;
    public static int APPELLO_ORDER_BY_AZ = 1;

    private static AppelloManager appelloManager = new AppelloManager();

    private AppelloManager(){}
    public static AppelloManager getInstance(){ return appelloManager; }

    public List<Appello> getAppelliData(){
        return Esse3Parser.getInstance().getAppelliData();
    }

    public List<Appello> getAppelliData(int orderBy){
        List<Appello> t = this.getAppelliData();
        if(orderBy == APPELLO_ORDER_BY_OLDEST)
            Collections.sort(t, new DataAppelloComparator());
        if(orderBy == APPELLO_ORDER_BY_AZ)
            Collections.sort(t, new NomeAppelloComparator());
        return t;
    }


    /* ORDERING */

    public static class DataAppelloComparator implements Comparator<Appello> {
        @Override
        public int compare(Appello a, Appello b) {
            //Ordina dal più nuovo
            return a.getData().compareTo(b.getData());
        }
    }

    public static class NomeAppelloComparator implements Comparator<Appello> {
        @Override
        public int compare(Appello a, Appello b) {
            //Ordina dal più nuovo
            return a.getNome().compareTo(b.getNome());
        }
    }
}
