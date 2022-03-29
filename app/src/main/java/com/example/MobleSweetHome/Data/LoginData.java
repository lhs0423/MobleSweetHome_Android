package com.example.MobleSweetHome.Data;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("id")
    String id;

    @SerializedName("pw")
    String pw;

    public LoginData(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public LoginData(String id) {
        this.id = id;
    }
}
