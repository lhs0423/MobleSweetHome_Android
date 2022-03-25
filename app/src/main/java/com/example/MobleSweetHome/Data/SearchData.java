package com.example.MobleSweetHome.Data;

import com.google.gson.annotations.SerializedName;

public class SearchData {

    @SerializedName("email")
    String email;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;


    public SearchData(String email){
        this.email = email;
    }

    public SearchData(String id, String name){
        this.id = id;
        this.name = name;
    }

}
