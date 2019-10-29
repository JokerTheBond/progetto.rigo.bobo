package com.example.rigobobo.Parser;

import com.example.rigobobo.Model.Prenotazione;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrenotazioniParser {

    private static List<Prenotazione> Prenotazioni;

    public static List<Prenotazione> getPrenotazioniData(Document doc){
        Prenotazioni = new ArrayList<>();
        if(doc == null) return Prenotazioni;
        //TODO Cosa succede se metto una classe sbagliata qua?
        Elements tables = doc.getElementsByClass("detail_table").select("table");
        for(Element table: tables) {
            PrenotazioneParser t = new PrenotazioneParser(table);
            if (t.validInstance) {
                Prenotazioni.add(t.convertToPrenotazione());
            }
        }
        return Prenotazioni;
    }

    private static class PrenotazioneParser extends Prenotazione{

        private Boolean validInstance;

        private PrenotazioneParser(Element table){
            this.validInstance = false;
            String nomeContent, descContent, dataContent, oraContent, edificioContent, aulaContent;
            Pattern pattern;
            Matcher matcher;
            try {
                table = table.child(0);
                nomeContent = table.child(0).child(0).text();
                descContent = table.child(3).child(0).text();
                dataContent = table.child(6).child(0).text();
                oraContent = table.child(6).child(1).text();
                edificioContent = table.child(6).child(2).text();
                aulaContent = table.child(6).child(3).text();

            }
            catch (Exception e){ return; }

            //Nome
            pattern = Pattern.compile("^(.*)\\s\\-\\s(.*)\\s\\-\\s(.*)$");
            matcher = pattern.matcher(nomeContent);
            if( !matcher.find() ) return;
            this.nome = matcher.group(1);

            //Parse data
            try {
                dataContent += " " + oraContent;
                this.data = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataContent);
            }
            catch (Exception e){ return; }

            //Altro
            if( !descContent.equals("") ) this.desc = descContent;
            if( !edificioContent.equals("") ) this.edificio = edificioContent;
            if( !aulaContent.equals("") ) this.aula = aulaContent;

            this.validInstance = true;
        }

        private Prenotazione convertToPrenotazione(){ return this; }
    }
}
