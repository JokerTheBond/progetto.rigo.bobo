package com.example.rigobobo.Parser;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import android.content.SharedPreferences;
import android.util.Base64;

import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.Model.Info;
import com.example.rigobobo.Model.Prenotazione;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.Model.Voto;
import com.example.rigobobo.View.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/*
 * Innanzitutto definisco nella classe le credenziale per il portale
 * Dopodichè creo una funzione per l'accesso ad una pagina generica
 * Quindi creo n funzioni, una per ogni pagina della quale ho bisogno di fare il parsing
 */

public class Esse3Parser {

    private static Esse3Parser parser = new Esse3Parser();
    private String username = null;
    private String password = null;

    private Esse3Parser() {
        System.out.println("hello1");
        SharedPreferences sharedPreferences =
                MainActivity.getContext().getSharedPreferences("login", MODE_PRIVATE);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            System.out.println("hello3");
            username = sharedPreferences.getString("username", "");
            password = sharedPreferences.getString("password", "");
        }
    }

    public static Esse3Parser getInstance(){ return parser; }

    public Esse3Parser setCredentials(String username, String password){
        this.username = username;
        this.password = password;
        if(username == null && password == null) MainActivity.getContext().deleteSharedPreferences("login");
        return this;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean checkCredentials(){
        System.out.println("hello4");
        return (username != null && password != null);
    }


    private Document getEsse3Doc(String url){
        Response res;
        Document doc;

        //Prima richiesta per ottenere JSESSIONID
        try {
            res = Jsoup.connect("https://www.esse3.unimore.it/Home.do").execute();
        }
        catch(IOException e){
            System.out.println("Home.do download failed:");
            System.out.println(e);
            return null;
        }
        String jsessionid = res.cookie("JSESSIONID");

        //Setup authorization header
        String login = this.username + ":" + this.password;
        String base64login = Base64.encodeToString(login.getBytes(), Base64.DEFAULT);

        //Il segreto è ripetere la richiesta 2 volte!
        try {
            Connection con = Jsoup.connect(url)
                    .header("Authorization", "Basic " + base64login)
                    .cookie("JSESSIONID", jsessionid);
            con.execute();
        }
        catch(IOException e){ }
        try {
            Connection con = Jsoup.connect(url)
                    .header("Authorization", "Basic " + base64login)
                    .cookie("JSESSIONID", jsessionid);
            doc = con.get();
        }
        catch(IOException e){
            System.out.println("Document download failed:");
            e.printStackTrace();
            return null;
        }

        return doc;
    }


    public Info getInfoData(){
        Document doc;
        try {
            doc = getEsse3Doc("https://www.esse3.unimore.it/auth/studente/AreaStudente.do");
        }
        catch (Exception e){ return null; }

        return InfoParser.getInfoData(doc);
    }

    public List<Appello> getAppelliData(){
        Document doc;
        try {
            doc = getEsse3Doc("https://www.esse3.unimore.it/auth/studente/Appelli/AppelliF.do");
        }
        catch (Exception e){ return new ArrayList<>(); }

        return AppelliParser.getAppelliData(doc);
    }

    public List<Voto> getVotiData(){
        //TODO Al momento se ci sono errori torna un array vuoto
        //devo fare un raise e mostrare un alert
        //inoltre devo impostare un timeout
        Document doc;
        try {
            doc = getEsse3Doc("https://www.esse3.unimore.it/auth/studente/Libretto/LibrettoHome.do");
        }
        catch (Exception e){ return new ArrayList<>(); }

        return VotiParser.getVotiData(doc);
    }

    public List<Prenotazione> getPrenotazioniData(){
        Document doc;
        try {
            doc = getEsse3Doc("https://www.esse3.unimore.it/auth/studente/Appelli/BachecaPrenotazioni.do");
        }
        catch (Exception e){ return new ArrayList<>(); }

        return PrenotazioniParser.getPrenotazioniData(doc);
    }

    public List<Tassa> getTasseData(){
        Document doc;
        try {
            doc = getEsse3Doc("https://www.esse3.unimore.it/auth/studente/Tasse/ListaFatture.do");
        }
        catch (Exception e){ return new ArrayList<>(); }

        return TasseParser.getTasseData(doc);
    }
}
