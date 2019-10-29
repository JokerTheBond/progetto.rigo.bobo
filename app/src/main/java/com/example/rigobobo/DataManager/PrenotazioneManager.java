package com.example.rigobobo.DataManager;

import com.example.rigobobo.Model.Prenotazione;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.List;

public class PrenotazioneManager {

    private static PrenotazioneManager prenotazioneManager = new PrenotazioneManager();

    private PrenotazioneManager (){ }
    public static PrenotazioneManager getInstance(){ return prenotazioneManager; }

    public List<Prenotazione> getPrenotazioniData(){
        try {
            return Esse3Parser.getInstance().getPrenotazioniData();
        }
        catch (Exception e){
            throw e;
        }
    }

}
