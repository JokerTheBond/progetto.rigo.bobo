package com.example.rigobobo.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Appello {

    protected String nome;
    protected Date data;
    protected String desc = null;

    public Appello (){}

    public Appello(String nome, Date data, String desc){
        this.nome = nome;
        this.data = data;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return (getNome() + " - " + getData().toString() + ": " + getDesc());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) { return true; }
        if ( !(obj instanceof Appello) ) { return false; }
        Appello guest = (Appello) obj;
        if(this.nome != guest.nome) return false;
        if(this.data != guest.data) return false;
        if(this.desc != guest.desc) return false;
        return true;
    }

    public String getNome() { return nome; }
    public Date getData() { return data; }
    public String getDataFormatted() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(getData());
    }
    public String getDesc() { return desc; }

}