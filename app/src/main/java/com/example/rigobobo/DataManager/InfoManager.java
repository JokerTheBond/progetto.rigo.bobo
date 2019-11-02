package com.example.rigobobo.DataManager;

import com.example.rigobobo.Database.InfoHelper;
import com.example.rigobobo.Model.Info;
import com.example.rigobobo.Parser.Esse3Parser;


public class InfoManager {

    private static InfoManager infoManager = new InfoManager();
    private static InfoHelper infoHelper = new InfoHelper();

    private InfoManager(){}
    public static InfoManager getInstance(){ return infoManager; }

    public Info getInfoData(){
        if( infoHelper.getInfo() == null ) {
            return getInfoData(true);
        }
        else return infoHelper.getInfo();
    }
    public Info getInfoData(Boolean forceUpdate){
        if(forceUpdate) {
            Info info;
            try {
                info = Esse3Parser.getInstance().getInfoData();
            } catch (Exception e) {
                throw e;
            }
            if(info != null) {
                //Se non ci sono stati errori nel download dei nuovi voti aggiorno il DB
                infoHelper.setInfo(info);
            }
            return info;
        }
        else return getInfoData();
    }
}
