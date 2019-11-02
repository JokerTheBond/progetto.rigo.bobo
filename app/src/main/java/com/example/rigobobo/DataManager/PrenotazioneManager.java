package com.example.rigobobo.DataManager;

import com.example.rigobobo.Database.PrenotazioneHelper;
import com.example.rigobobo.Model.Prenotazione;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.List;

public class PrenotazioneManager {

    private static PrenotazioneManager prenotazioneManager = new PrenotazioneManager();
    private static PrenotazioneHelper prenotazioneHelper = new PrenotazioneHelper();

    private PrenotazioneManager (){ }
    public static PrenotazioneManager getInstance(){ return prenotazioneManager; }

    public List<Prenotazione> getPrenotazioniData(){
        if( prenotazioneHelper.getAll().size() == 0 ) {
            return getPrenotazioniData(true);
        }
        else return prenotazioneHelper.getAll();
    }
    public List<Prenotazione> getPrenotazioniData(Boolean forceUpdate){
        if(forceUpdate) {
            List<Prenotazione> prenotazioni;
            try {
                prenotazioni = Esse3Parser.getInstance().getPrenotazioniData();
            } catch (Exception e) {
                throw e;
            }
            if(prenotazioni != null && prenotazioni.size() > 0) {
                //Se non ci sono stati errori nel download dei nuovi prenotazioni aggiorno il DB
                prenotazioneHelper.flushTable();
                prenotazioneHelper.addPrenotazioni(prenotazioni);
            }
            return prenotazioni;
        }
        else return getPrenotazioniData();
    }

}
