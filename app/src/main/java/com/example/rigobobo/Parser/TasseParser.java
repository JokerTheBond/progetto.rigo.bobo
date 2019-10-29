package com.example.rigobobo.Parser;

import com.example.rigobobo.Model.Tassa;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TasseParser {

    private static List<Tassa> Tasse;

    public static List<Tassa> getTasseData(Document doc){
        Tasse = new ArrayList<>();
        if(doc == null) return Tasse;
        Elements elements = doc.getElementsByAttributeValueContaining("src", "semaf_r.gif");
        //For testing
        //Elements elements = doc.getElementsByAttributeValueContaining("src", "semaf_v.gif");
        for(Element ele: elements) {
            //Skip legends element
            if (!ele.hasAttr("alt")) continue;

            TassaParser t = new TassaParser(ele);
            if (t.validInstance) {
                Tasse.add(t.convertToTassa());
            }
        }
        return Tasse;
    }

    private static class TassaParser extends Tassa{

        private Boolean validInstance;

        private TassaParser(Element ele){
            this.validInstance = false;
            String valueContent, scadenzaContent;
            Pattern pattern;
            Matcher matcher;
            try {
                valueContent = ele.parent()
                        .previousElementSibling()
                        .attr("data-sort-value");
                scadenzaContent = ele.parent()
                        .previousElementSibling()
                        .previousElementSibling().text();
            }
            catch (Exception e){ return; }

            //Parse
            try {
                this.importo = Float.parseFloat(valueContent);
                this.scadenza = new SimpleDateFormat("dd/MM/yyyy").parse(scadenzaContent);
            }
            catch (Exception e){ return; }

            this.validInstance = true;
        }

        private Tassa convertToTassa(){ return this; }
    }

}
