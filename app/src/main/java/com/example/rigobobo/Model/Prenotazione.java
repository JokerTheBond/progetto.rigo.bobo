package com.example.rigobobo.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Prenotazione {

    protected String nome;
    protected String desc = null;
    protected Date data;
    protected String edificio = null;
    protected String aula = null;

    public Prenotazione(){}

    public Prenotazione(String nome, String desc, Date data, String edificio, String aula){
        this.nome = nome;
        this.desc = desc;
        this.data = data;
        this.edificio = edificio;
        this.aula = aula;
    }

    @Override
    public String toString() {
        return (getNome() + " - " + getData().toString() + ": " + getDesc());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) { return true; }
        if ( !(obj instanceof Prenotazione) ) { return false; }
        Prenotazione guest = (Prenotazione) obj;
        if( !this.nome.equals(guest.nome) ) return false;
        if( !this.data.equals(guest.data) ) return false;
        if( !this.desc.equals(guest.desc) ) return false;
        if(this.edificio != guest.edificio) return false;
        if( !this.aula.equals(guest.aula) ) return false;
        return true;
    }

    public String getNome() { return nome; }
    public String getDesc() { return desc; }
    public Date getData() { return data; }
    public String getDataOraFormatted() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'alle' HH:mm");
        return format.format(getData());
    }
    public String getEdificio() { return edificio; }
    public String getAula() { return aula; }
}
