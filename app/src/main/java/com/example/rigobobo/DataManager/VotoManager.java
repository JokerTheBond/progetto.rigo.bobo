package com.example.rigobobo.DataManager;

import com.example.rigobobo.Database.VotoHelper;
import com.example.rigobobo.Model.Voto;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VotoManager {

    public static int VOTO_ORDER_BY_HIGHEST = 0;
    public static int VOTO_ORDER_BY_NEWEST = 1;

    private static VotoManager votoManager = new VotoManager();
    private static VotoHelper votoHelper = new VotoHelper();

    private VotoManager (){ }
    public static VotoManager getInstance(){ return votoManager; }

    public List<Voto> getVotiData(){
        return votoHelper.getAll();
    }
    public List<Voto> getVotiData(Boolean forceUpdate){
        if(forceUpdate) {
            List<Voto> voti;
            try {
                voti = Esse3Parser.getInstance().getVotiData();
            } catch (Exception e) {
                throw e;
            }
            if(voti != null && voti.size() > 0) {
                //Se non ci sono stati errori nel download dei nuovi voti aggiorno il DB
                votoHelper.flushTable();
                votoHelper.addVoti(voti);
            }
            return voti;
        }
        else return getVotiData();
    }
    public List<Voto> getVotiData(int orderBy){
        List<Voto> voti = this.getVotiData();
        if (orderBy == VOTO_ORDER_BY_NEWEST)
            Collections.sort(voti, new DataVotoComparator());
        if (orderBy == VOTO_ORDER_BY_HIGHEST)
            Collections.sort(voti, new VotoComparator());
        return voti;
    }

    public int getCreditiConseguiti(){
        //TODO Qui usare cache
        List<Voto> voti = getVotiData();
        int crediti = 0;
        for(Voto voto: voti){
            crediti += voto.getCrediti();
        }
        return crediti;
    }

    public float getMediaPesata(){
        //TODO Qui usare cache
        List<Voto> voti = getVotiData();
        int crediti = getCreditiConseguiti();
        float media = 0;
        for(Voto voto: voti){
            media += voto.getVotoI() * 1.0 * voto.getCrediti() / crediti;
        }
        return media;
    }


    /* COMPARATOR */

    public static class VotoComparator implements Comparator<Voto> {
        @Override
        public int compare(Voto a, Voto b) {
            return a.getVotoI() > b.getVotoI() ? -1 : a.getVotoI() < b.getVotoI() ? 1 :
                    a.getLode() && !b.getLode() ? -1 : !a.getLode() && b.getLode() ? 1 : 0;
        }
    }

    public static class DataVotoComparator implements Comparator<Voto> {
        @Override
        public int compare(Voto a, Voto b) {
            //Ordina dal più nuovo
            return b.getData().compareTo(a.getData());
        }
    }
}
