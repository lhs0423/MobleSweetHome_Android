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
//    @GET("/retrofit/get")
//    Call<ResponseBody> getFunc(@Query("data") String data);


    // 회원가입
    @POST("/retrofit/post/login") // 로그인
    Call<ResponseBody> LoginFunc(@Body LoginData data);

    @POST("/retrofit/post/signup") // 회원가입
    Call<ResponseBody> SignupFunc(@Body SignupData data);

    @POST("/retrofit/post/search_id") // 아이디 찾기
    Call<ResponseBody> Search_idFunc(@Body SearchData data);

    @POST("/retrofit/post/search_pw") // 비밀번호 찾기
    Call<ResponseBody> Search_pwFunc(@Body SearchData data);

    @POST("/retrofit/post/update_pw") // 비밀번호 변경
    Call<ResponseBody> Update_pwFunc(@Body LoginData data);


    // 라즈베리파이
    @POST("retrofit/post/room1_led") // 조명제어 - 거실 LED
    Call<ResponseBody> Room1_Func(@Body RaspiData data);

    @POST("retrofit/post/room2_led") // 조명제어 - 화장실 LED
    Call<ResponseBody> Room2_Func(@Body RaspiData data);

    @POST("retrofit/post/room3_led") // 조명제어 - 방1 LED
    Call<ResponseBody> Room3_Func(@Body RaspiData data);

    @POST("retrofit/post/temp_humi") // 온습도센서
    Call<RaspiResponse> Temp_Humi(@Body RaspiData data);

    @POST("retrofit/post/led1_check") // 거실 LED 상태 체크
    Call<RaspiData> Led_Check1(@Body RaspiData data);

    @POST("retrofit/post/led2_check") // 화장실 LED 상태 체크
    Call<RaspiData> Led_Check2(@Body RaspiData data);

    @POST("retrofit/post/led3_check") // 방1 LED 상태 체크
    Call<RaspiData> Led_Check3(@Body RaspiData data);

    @POST("retrofit/post/dust") // 미세먼지센서
    Call<RaspiResponse> Dust_Func(@Body RaspiData data);

    @POST("retrofit/post/gas") // 가스센서
    Call<RaspiResponse> Gas_Func(@Body RaspiData data);

    @POST("retrofit/post/out_info") // 로그인한 사용자 ID로 DB에 저장된 주소의 정보를 가져와 외부 날씨를 웹 크롤링 하는데 이용
    Call<ResponseBody> userId_Func(@Body LoginData data);


//    @FormUrlEncoded
//    @PUT("/retrofit/put/{id}")
//    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);
//
//    @DELETE("/retrofit/delete/{id}")
//    Call<ResponseBody> deleteFunc(@Path("id") String id);
}
