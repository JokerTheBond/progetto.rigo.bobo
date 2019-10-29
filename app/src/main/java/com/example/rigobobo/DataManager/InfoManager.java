package com.example.rigobobo.DataManager;

import com.example.rigobobo.Model.Info;
import com.example.rigobobo.Parser.Esse3Parser;


public class InfoManager {

    private static InfoManager infoManager = new InfoManager();

    private InfoManager(){}
    public static InfoManager getInstance(){ return infoManager; }

    public Info getInfoData(){
        return Esse3Parser.getInstance().getInfoData();
    }

}
