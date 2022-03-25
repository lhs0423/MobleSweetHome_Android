package com.example.MobleSweetHome.Data;

import com.google.gson.annotations.SerializedName;

public class RaspiData {

    @SerializedName("state")
    Boolean state;

    @SerializedName("check")
    int check;

    public RaspiData(Boolean state) {
        this.state = state;
    }

    public RaspiData() { }

    public int getCheck() {return check;}




}
