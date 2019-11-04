package com.example.rigobobo.DataManager;

import com.example.rigobobo.Database.TassaHelper;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.List;

public class TassaManager {

    private static TassaManager TassaManager = new TassaManager();
    private static TassaHelper tassaHelper = new TassaHelper();

    private TassaManager (){ }
    public static TassaManager getInstance(){ return TassaManager; }

    public List<Tassa> getTasseData(){
        return tassaHelper.getAll();
    }
    public List<Tassa> getTasseData(Boolean forceUpdate){
        if(forceUpdate) {
            List<Tassa> tasse;
            try {
                tasse = Esse3Parser.getInstance().getTasseData();
            } catch (Exception e) {
                throw e;
            }
            if(tasse != null) {
                //Se non ci sono stati errori nel download dei nuovi voti aggiorno il DB
                tassaHelper.flushTable();
                tassaHelper.addTasse(tasse);
            }
            return tasse;
        }
        else return getTasseData();
    }

}
