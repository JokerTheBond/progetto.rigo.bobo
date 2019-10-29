package com.example.rigobobo.Parser;

import com.example.rigobobo.Model.Info;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoParser {

    public static Info getInfoData(Document doc){
        if(doc == null) return null;
        InfoParserObj t = new InfoParserObj(doc);
        if (!t.validInstance) { return null; }
        return t;
    }

    private static class InfoParserObj extends Info {

        private Boolean validInstance;

        private InfoParserObj(Document doc){
            this.validInstance = false;
            String nomeContent, corsoContent, annoContent, imgContent;
            Pattern pattern;
            Matcher matcher;
            try {
                nomeContent = doc.getElementsByClass("masthead_usermenu_user_name").text();
                corsoContent = doc.getElementsByClass("record-riga").first().child(9).text();
                annoContent = doc.getElementsByClass("record-riga").first().child(5).text();
                imgContent = doc.getElementsByClass("masthead_usermenu_user").first()
                    .getElementsByTag("img").first().attr("src");

            }
            catch (Exception e){ return; }

            //Nome
            this.nome = "";
            String[] pieces = nomeContent.split("\\s");
            for(String piece: pieces){
                if(!piece.equals(pieces[0])) this.nome += " ";
                this.nome += piece.substring(0, 1) + piece.substring(1).toLowerCase();
            }

            //Corso
            pattern = Pattern.compile("\\]\\s\\-\\s(.*)\\s\\(.*");
            matcher = pattern.matcher(corsoContent);
            if( !matcher.find() ) return;
            this.corsoDiStudio = matcher.group(1);

            //Anno
            this.anno = Integer.parseInt(annoContent);

            //FotoProfilo
            this.fotoProfilo = "https://www.esse3.unimore.it/" + imgContent;

            this.validInstance = true;
        }

        private Info convertToInfo(){ return this; }
    }
}
