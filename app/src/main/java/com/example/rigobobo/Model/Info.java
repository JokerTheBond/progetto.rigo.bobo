package com.example.rigobobo.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Info {

    protected String nome;
    protected String corsoDiStudio;
    protected int anno;
    protected String fotoProfilo;

    public Info (){}

    public Info(String nome, String corsoDiStudio, int anno, String fotoProfilo) {
        this.nome = nome;
        this.corsoDiStudio = corsoDiStudio;
        this.anno = anno;
        this.fotoProfilo = fotoProfilo;
    }

    @Override
    public String toString() {
        return (getNome() + " - " + getCorsoDiStudio() + " - " + getAnnoStr() + " - " + getFotoProfilo());
    }

    public String getNome() {
        return nome;
    }

    public String getCorsoDiStudio() {
        return corsoDiStudio;
    }

    public int getAnno() {
        return anno;
    }
    public String getAnnoStr() {
        return anno + "° anno";
    }

    public String getFotoProfilo() {
        return fotoProfilo;
    }
}