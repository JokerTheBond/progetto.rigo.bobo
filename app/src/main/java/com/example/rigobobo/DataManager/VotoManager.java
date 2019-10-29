package com.example.rigobobo.DataManager;

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

    private VotoManager (){ }
    public static VotoManager getInstance(){ return votoManager; }

    public List<Voto> getVotiData(){
        try {
            return Esse3Parser.getInstance().getVotiData();
        }
        catch (Exception e){
            throw e;
        }
    }

    private List<Voto> voti = new ArrayList<>();
    public List<Voto> getVotiData(int orderBy){
        //TODO rimuovi questo fix
        if(voti.size() == 0) {
            List<Voto> t = this.getVotiData();
            if (orderBy == VOTO_ORDER_BY_NEWEST)
                Collections.sort(t, new DataVotoComparator());
            if (orderBy == VOTO_ORDER_BY_HIGHEST)
                Collections.sort(t, new VotoComparator());
            voti = t;
            return t;
        }
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
            media += voto.getVotoI() / crediti;
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
