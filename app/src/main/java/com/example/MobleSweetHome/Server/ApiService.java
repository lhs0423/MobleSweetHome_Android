package com.example.MobleSweetHome.Server;

import com.example.MobleSweetHome.Data.LoginData;
import com.example.MobleSweetHome.Data.RaspiData;
import com.example.MobleSweetHome.Data.RaspiResponse;
import com.example.MobleSweetHome.Data.SearchData;
import com.example.MobleSweetHome.Data.SignupData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/retrofit/get")
    Call<ResponseBody> getFunc(@Query("data") String data);


    // 회원가입
    @POST("/retrofit/post/login")
    Call<ResponseBody> LoginFunc(@Body LoginData data);

    @POST("/retrofit/post/signup")
    Call<ResponseBody> SignupFunc(@Body SignupData data);

    @POST("/retrofit/post/search_id")
    Call<ResponseBody> Search_idFunc(@Body SearchData data);

    @POST("/retrofit/post/search_pw")
    Call<ResponseBody> Search_pwFunc(@Body SearchData data);

    @POST("/retrofit/post/update_pw")
    Call<ResponseBody> Update_pwFunc(@Body LoginData data);


    // 라즈베리파이
    @POST("retrofit/post/room1_led")
    Call<ResponseBody> Room1_Func(@Body RaspiData data);

    @POST("retrofit/post/room2_led")
    Call<ResponseBody> Room2_Func(@Body RaspiData data);

    @POST("retrofit/post/room3_led")
    Call<ResponseBody> Room3_Func(@Body RaspiData data);

    @POST("retrofit/post/temp_humi")
    Call<RaspiResponse> Temp_Humi(@Body RaspiData data);

    @POST("retrofit/post/led1_check")
    Call<RaspiData> Led_Check1(@Body RaspiData data);

    @POST("retrofit/post/led2_check")
    Call<RaspiData> Led_Check2(@Body RaspiData data);

    @POST("retrofit/post/led3_check")
    Call<RaspiData> Led_Check3(@Body RaspiData data);

    @POST("retrofit/post/dust")
    Call<RaspiResponse> Dust_Func(@Body RaspiData data);

    @POST("retrofit/post/test2")
    Call<ResponseBody> test2_Func(@Body RaspiData data);



    @FormUrlEncoded
    @PUT("/retrofit/put/{id}")
    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);

    @DELETE("/retrofit/delete/{id}")
    Call<ResponseBody> deleteFunc(@Path("id") String id);
}