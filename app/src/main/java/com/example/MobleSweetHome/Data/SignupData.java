package com.example.MobleSweetHome.Data;

import com.google.gson.annotations.SerializedName;

public class SignupData {
    @SerializedName("id")
    String id;

    @SerializedName("pw")
    String pw;

    @SerializedName("name")
    String name;

    @SerializedName("birth")
    String birth;

    @SerializedName("number")
    String number;

    @SerializedName("address")
    String address;

    @SerializedName("email")
    String email;

    public SignupData(String id, String pw, String name, String birth, String number, String address, String email){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.birth = birth;
        this.number = number;
        this.address = address;
        this.email = email;
    }
}
