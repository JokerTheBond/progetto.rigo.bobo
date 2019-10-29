package com.example.rigobobo.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Voto {

    protected String nome;
    protected int voto;
    protected Boolean lode;
    protected Date data;
    protected int crediti;

    public Voto(){}

    public Voto(String nome, int voto, Boolean lode, Date data, int crediti){
        this.nome = nome;
        this.voto = voto;
        this.lode = lode;
        this.data = data;
        this.crediti = crediti;
    }

    @Override
    public String toString() {
        return (getVoto() + " - " + nome + data.toString());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) { return true; }
        if ( !(obj instanceof Voto) ) { return false; }
        Voto guest = (Voto) obj;
        if(this.nome != guest.nome) return false;
        if(this.data != guest.data) return false;
        if(this.voto != guest.voto) return false;
        if(this.lode != guest.lode) return false;
        if(this.crediti != guest.crediti) return false;
        return true;
    }

    public String getNome() { return nome; }
    public String getVoto() {
        //Lascia la logica della lode all'interno della classe
        String lode = "";
        if(this.lode) lode = "L";
        return voto + lode;
    }
    public float getVotoI(){ return voto; }
    public Boolean getLode(){ return lode; }
    public Date getData() { return data; }
    public String getDataFormatted() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(getData());
    }
    public int getCrediti() { return crediti; }

}
