package com.example.MobleSweetHome.Server;

import com.example.MobleSweetHome.Server.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    public String TAG = "Retrofit";
    final String URL = "http://192.168.0.23:8888/"; // moble local ip
//    final String URL = "http://172.30.1.30:8888/"; // 안산 local ip
//    final String URL = "http://192.168.0.59:8888/"; // 기민 local ip
//    final String URL = "http://35.247.4.224:8888/"; // 클라우드 ip


    Retrofit retrofit = new Retrofit.Builder() // 레트로핏
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ApiService service = retrofit.create(ApiService.class); // 통신서비스
}
