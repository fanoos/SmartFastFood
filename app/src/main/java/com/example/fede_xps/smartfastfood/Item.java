package com.example.fede_xps.smartfastfood;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Fede-xps on 13/04/2017.
 */

public class Item implements Serializable{

    private transient JSONObject json;
    private boolean isCheck;

    public Item (JSONObject j) {
        json=j;
        isCheck=false;
    }

    public void setCheck(boolean b) {
        isCheck=b;
    }

    public boolean getCheck() {
        return isCheck;
    }

    public JSONObject getJson() {
        return json;
    }


}
