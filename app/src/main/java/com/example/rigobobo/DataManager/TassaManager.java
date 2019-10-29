package com.example.rigobobo.DataManager;

import com.example.rigobobo.Model.Tassa;
import com.example.rigobobo.Parser.Esse3Parser;

import java.util.List;

public class TassaManager {
    private static TassaManager TassaManager = new TassaManager();

    private TassaManager (){ }
    public static TassaManager getInstance(){ return TassaManager; }

    public List<Tassa> getTasseData(){
        try {
            return Esse3Parser.getInstance().getTasseData();
        }
        catch (Exception e){
            throw e;
        }
    }

}
