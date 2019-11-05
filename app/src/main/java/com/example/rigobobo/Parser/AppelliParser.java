package com.example.rigobobo.Parser;

import com.example.rigobobo.Model.Appello;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AppelliParser {

    private static List<Appello> Appelli;

    public static List<Appello> getAppelliData(Document doc){
        Appelli = new ArrayList<>();
        if(doc == null) return Appelli;
        Elements rows = doc.getElementById("app-tabella_appelli").getElementsByTag("tr");
        for(Element row: rows) {
            AppelloParser t = new AppelloParser(row);
            if (t.validInstance) {
                Appelli.add(t.convertToAppello());
            }
        }
        return Appelli;
    }

    private static class AppelloParser extends Appello {

        private Boolean validInstance;

        private AppelloParser(Element row){
            this.validInstance = false;
            String nomeContent, dataContent, descContent;
            try {
                nomeContent = row.child(1).text();
                dataContent = row.child(2).text();
                descContent = row.child(4).text();
            }
            catch (Exception e){ return; }

            //Nome
            this.nome = nomeContent;

            //Parse data
            try {
                this.data = new SimpleDateFormat("dd/MM/yyyy").parse(dataContent);
            }
            catch (Exception e){ return; }

            //Altro
            if( !descContent.equals("") ) this.desc = descContent;

            this.validInstance = true;
        }

        private Appello convertToAppello(){ return this; }
    }
}
