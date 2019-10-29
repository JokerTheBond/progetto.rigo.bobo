package com.example.rigobobo.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tassa {

    protected float importo;
    protected Date scadenza;

    public Tassa(){}

    public Tassa(float importo, Date scadenza){
        this.importo = importo;
        this.scadenza = scadenza;
    }

    @Override
    public String toString() {
        return (getImporto() + " - " + getScadenza().toString());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) { return true; }
        if ( !(obj instanceof Tassa) ) { return false; }
        Tassa guest = (Tassa) obj;
        if(this.importo != guest.importo) return false;
        if(this.scadenza != guest.scadenza) return false;
        return true;
    }

    public float getImporto() { return importo; }
    public Date getScadenza() { return scadenza; }
    public String getScadenzaFormatted() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(getScadenza());
    }
}
