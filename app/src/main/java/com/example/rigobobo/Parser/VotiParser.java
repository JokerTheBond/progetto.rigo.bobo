package com.example.rigobobo.Parser;

import com.example.rigobobo.Model.Voto;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VotiParser {

    private static List<Voto> Voti;

    public static List<Voto> getVotiData(Document doc){
        Voti = new ArrayList<>();
        if(doc == null) return Voti;
        Elements elements = doc.getElementsByAttributeValueContaining("src", "superata.gif");
        for(Element ele: elements) {
            //Skip legends element
            if (!ele.hasAttr("alt")) continue;

            VotoParser t = new VotoParser(ele);
            if (t.validInstance) {
                Voti.add(t.convertToVoto());
            }
        }
        return Voti;
    }

    private static class VotoParser extends Voto{

        private Boolean validInstance;

        private VotoParser(Element ele){
            this.validInstance = false;
            String votoDataContent, creditiContent, nomeContent;
            Pattern pattern;
            Matcher matcher;
            try {
                votoDataContent = ele.parent()
                        .nextElementSibling()
                        .nextElementSibling().text();
                creditiContent = ele.parent()
                        .previousElementSibling()
                        .attr("data-sort-value");
                nomeContent = ele.parent()
                        .previousElementSibling()
                        .previousElementSibling()
                        .previousElementSibling()
                        .getElementsByTag("a").text();
            }
            catch (Exception e){ return; }

            //Nome
            pattern = Pattern.compile("^.*\\s\\-\\s(.*)$");
            matcher = pattern.matcher(nomeContent);
            if( !matcher.find() ) return;
            this.nome = matcher.group(1);

            //Voto e data
            pattern = Pattern.compile("^(.*)\\s\\-\\s(.*)$");
            matcher = pattern.matcher(votoDataContent);
            if( !matcher.find() ) return;
            String votoContent = matcher.group(1);
            String dataContent = matcher.group(2);

            //Voto
            pattern = Pattern.compile("^(\\d+)(L?)$");
            matcher = pattern.matcher(votoContent);
            if( !matcher.find() ) return;
            try {
                this.voto = Integer.parseInt(matcher.group(1));
            }
            catch (Exception e){ return; }
            String lodeContent = matcher.group(2);
            lode = lodeContent.equals("L");

            //Data
            try {
                this.data = new SimpleDateFormat("dd/MM/yyyy").parse(dataContent);
            }
            catch (Exception e){ return; }

            //Crediti
            try {
                this.crediti = Integer.parseInt(creditiContent);
            }
            catch (Exception e) { return; }

            this.validInstance = true;
        }

        private Voto convertToVoto(){ return this; }
    }

}
