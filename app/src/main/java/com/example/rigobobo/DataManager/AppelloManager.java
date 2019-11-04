package com.example.rigobobo.DataManager;

import com.example.rigobobo.Database.AppelloHelper;
import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppelloManager {

    public static int APPELLO_ORDER_BY_OLDEST = 0;
    public static int APPELLO_ORDER_BY_AZ = 1;

    private static AppelloManager appelloManager = new AppelloManager();
    private static AppelloHelper appelloHelper = new AppelloHelper();

    private AppelloManager(){}
    public static AppelloManager getInstance(){ return appelloManager; }

    public List<Appello> getAppelliData(){
        return appelloHelper.getAll();
    }
    public List<Appello> getAppelliData(Boolean forceUpdate){
        if(forceUpdate) {
            List<Appello> appelli;
            try {
                appelli = Esse3Parser.getInstance().getAppelliData();
            } catch (Exception e) {
                throw e;
            }
            if(appelli != null) {
                appelloHelper.flushTable();
                appelloHelper.addAppelli(appelli);
            }
            return appelli;
        }
        else return getAppelliData();
    }
    public List<Appello> getAppelliData(int orderBy){
        List<Appello> appelli = this.getAppelliData();
        if (orderBy == APPELLO_ORDER_BY_OLDEST)
            Collections.sort(appelli, new DataAppelloComparator());
        if (orderBy == APPELLO_ORDER_BY_AZ)
            Collections.sort(appelli, new NomeAppelloComparator());
        return appelli;
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
