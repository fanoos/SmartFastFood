package com.example.fede_xps.smartfastfood;

import java.io.Serializable;

/**
 * Created by Fede-xps on 23/05/2017.
 */

public class Item3 implements Serializable {

    private String code;
    private String pay;

    public Item3(String c, String p){
        code=c;
        pay=p;
    }

    public String getCode(){
        return code;
    }

    public String getPay(){
        return pay;
    }


}
