package com.example.MobleSweetHome.Data;

import com.google.gson.annotations.SerializedName;

public class RaspiResponse {

    @SerializedName("temp")
    int temp;

    @SerializedName("humi")
    int humi;

    @SerializedName("PM10")
    int PM10;

    @SerializedName("PM25")
    int PM25;

    @SerializedName("gas")
    int gas;

    public int getTemp() { return temp; }

    public int getHumi() {
        return humi;
    }

    public int getPM10() { return PM10; }

    public int getPM25() { return PM25; }

    public int getGas() { return  gas; }

}
