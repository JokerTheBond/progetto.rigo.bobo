package com.example.rigobobo.DataManager;

import com.example.rigobobo.Model.Appello;
import com.example.rigobobo.Model.Prenotazione;
import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.Model.Voto;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.Collections;
import java.util.List;

public class DataManager {

    /* TODO: in questa classe gestisco l'interfaccia per l'accesso al parser dall'app
    *   da qua gestisco cache ed eventuali errori di rete */

    private static DataManager dataManager = new DataManager();
    private Esse3Parser esse3Parser = Esse3Parser.getInstance();

    private String username;
    private String password;

    /* TODO: questo va fatto al login o all'apertura dell'app */
    private DataManager(){}
    public static DataManager getInstance() { return dataManager; }

    public DataManager setCredentials(String username, String password){
        esse3Parser.setCredentials(username, password);
        this.username = username;
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

