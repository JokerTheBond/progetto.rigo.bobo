package com.example.rigobobo.Model;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Notifica {

    public static final String TIPO_GENERIC = "generic";
    public static final String TIPO_VOTO = "voto";
    public static final String TIPO_TASSA = "tassa";
    public static final List<String> TIPI_DISPONIBILI = new ArrayList<>(Arrays.asList(
            TIPO_GENERIC, TIPO_VOTO, TIPO_TASSA));


    protected String titolo;
    protected String descrizione;
    protected String tipo;
    protected Date data_ora;
    protected Boolean vista;

    public Notifica(String titolo, String descrizione, String tipo, Date data_ora, Boolean vista){
        this.titolo = titolo;
        this.descrizione = descrizione;
        if( !TIPI_DISPONIBILI.contains(tipo) ) tipo = TIPO_GENERIC;
        this.tipo = tipo;
        this.data_ora = data_ora;
        this.vista = vista;
    }
    public Notifica(String titolo, String descrizione, String tipo, Date data_ora) {
        this(titolo, descrizione, tipo, data_ora, false);
    }

    public String getTitolo(){return titolo;}
    public String getDescrizione(){return descrizione;}
    public String getTipo(){return tipo;}
    public Date getDataOra(){return data_ora;}
    public String getDataOraStr(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'alle' HH:mm");
        return format.format(getDataOra());
    }
    public Boolean getVista(){return vista;}

    @NonNull
    @Override
    public String toString() {
        return getTitolo()+" - "+getDescrizione()+" - "+getTipo()+" - "+getDataOra().toString()+" - "+getVista().toString();
    }
}