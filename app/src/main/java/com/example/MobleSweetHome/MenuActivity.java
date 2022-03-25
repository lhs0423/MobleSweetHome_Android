package com.example.MobleSweetHome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.RaspiData;
import com.example.MobleSweetHome.Data.RaspiResponse;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    RetrofitService rs = new RetrofitService();
    Button lightbtn, windowbtn, firebtn, securebtn, logout, test;
    TextView user, temper, humid, pm10, pm25, temp_state, humi_state, pm10_state, pm25_state, internal_state;
    String userid;

    Boolean ctl = true;

    private long backKeyPressedTime = 0; // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private Toast toast; // 첫 번째 뒤로가기 버튼을 누를 때 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setting();

        lightbtn.setOnClickListener(this);
        securebtn.setOnClickListener(this);
        windowbtn.setOnClickListener(this);
        securebtn.setOnClickListener(this);
        logout.setOnClickListener(this);
        test.setOnClickListener(this);

        Intent intent = getIntent();
        userid = intent.getStringExtra("user");
        user.setText(userid + "님 환영합니다!");

        Internalinfo();
    }

    public void setting() {
        lightbtn = (Button) findViewById(R.id.btn_lightctl);
        securebtn = (Button)findViewById(R.id.btn_securectl);
        windowbtn = (Button)findViewById(R.id.btn_windowctl);
        firebtn = (Button)findViewById(R.id.btn_firectl);
        logout = (Button)findViewById(R.id.btn_logout);
        user = (TextView)findViewById(R.id.tv_user);
        temper = (TextView)findViewById(R.id.tv_temper);
        humid = (TextView)findViewById(R.id.tv_humid);
        pm10 = (TextView)findViewById(R.id.tv_pm10);
        pm25 = (TextView)findViewById(R.id.tv_pm25);
        temp_state = (TextView)findViewById(R.id.tv_temp_state);
        humi_state = (TextView)findViewById(R.id.tv_humi_state);
        pm10_state = (TextView)findViewById(R.id.tv_pm10_state);
        pm25_state = (TextView)findViewById(R.id.tv_pm25_state);
        internal_state = (TextView)findViewById(R.id.internal_state);

        test = (Button)findViewById(R.id.btn_test);
    }

    public void Internalinfo() { // 온습도, 미세먼지 정보
        Thread thread = new Thread("temp and humi Thread") {

            @Override
            public void run() {
                super.run();

                while(ctl) {
                    rs.service.Temp_Humi(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                        @Override
                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                            if(response.isSuccessful()) {
                                RaspiResponse result = response.body();
                                if(result.getTemp() < 0 && result.getHumi() < 0) { // 미세먼지 추가
                                    temp_state.setText("* 센서 접속 실패");
                                    humi_state.setText("* 센서 접속 실패");
                                    internal_state.setText("* 센서 접속 실패");
                                } else {
                                    temper.setText(String.valueOf(result.getTemp()));
                                    humid.setText(String.valueOf(result.getHumi()));
                                    temp_state.setText("");
                                    humi_state.setText("");
                                    internal_state.setText("");
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<RaspiResponse> call, Throwable t) {
                            Log.v(rs.TAG, "[MenuActivity] temp & humi : Fail");
                        }
                    });

                    rs.service.Dust_Func(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                        @Override
                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                            if(response.isSuccessful()) {
                                RaspiResponse result = response.body();
                                if(result.getPM10() < 0 && result.getPM25() < 0) {
                                    pm10_state.setText("* 센서 접속 실패");
                                    pm25_state.setText("* 센서 접속 실패");
                                    internal_state.setText("*센서 접속 실패");
                                } else {
                                    pm10.setText(String.valueOf(result.getPM10()));
                                    pm25.setText(String.valueOf(result.getPM25()));

                                    // 미세먼지(PM10)
                                    if(result.getPM10() <= 30) {
                                        pm10_state.setText("[좋음]");
                                        pm10_state.setTextColor(Color.parseColor("#32a1ff"));
                                    }
                                    else if(result.getPM10() <= 80) {
                                        pm10_state.setText("[보통]");
                                        pm10_state.setTextColor(Color.parseColor("#00c73c"));
                                    }
                                    else if(result.getPM10() <= 150) {
                                        pm10_state.setText("[나쁨]");
                                        pm10_state.setTextColor(Color.parseColor("#fd9b5a"));
                                    }
                                    else {
                                        pm10_state.setText("[매우나쁨]");
                                        pm10_state.setTextColor(Color.parseColor("#fd5959"));
                                    }

                                    // 초미세먼지(PM25)
                                    if(result.getPM25() <= 15) {
                                        pm25_state.setText("[좋음]");
                                        pm25_state.setTextColor(Color.parseColor("#32a1ff"));
                                    }
                                    else if(result.getPM25() <= 35) {
                                        pm25_state.setText("[보통]");
                                        pm25_state.setTextColor(Color.parseColor("#00c73c"));
                                    }
                                    else if(result.getPM25() <= 75) {
                                        pm25_state.setText("[나쁨]");
                                        pm25_state.setTextColor(Color.parseColor("#fd9b5a"));
                                    }
                                    else {
                                        pm25_state.setText("[매우나쁨]");
                                        pm25_state.setTextColor(Color.parseColor("#fb5959"));
                                    }
                                    internal_state.setText("");
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<RaspiResponse> call, Throwable t) {
                            Log.v(rs.TAG, "[MenuActivity] dust : Fail");
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lightctl:
                Intent light = new Intent(getApplicationContext(),LightActivity.class);
                startActivity(light);
//                startActivityForResult(light, REQUEST_CODE_LIGHT);
                break;
            case R.id.btn_securectl:
                Intent secure = new Intent(getApplicationContext(),SecureActivity.class);
                startActivity(secure);
                break;
            case R.id.btn_windowctl:
                break;
            case R.id.btn_firectl:
                break;
            case R.id.btn_logout:
                ctl = false; // 스레드 종료
                Toast.makeText(getApplicationContext(), "로그아웃이 되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 액티비티 종료, 추후 서버 연동 고려?
                break;
            case R.id.btn_test:

                rs.service.Temp_Humi(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                    @Override
                    public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                        if(response.isSuccessful()) {
                            RaspiResponse result = response.body();
                            temper.setText(String.valueOf(result.getTemp()));
                            humid.setText(String.valueOf(result.getHumi()));
                        }
                    }
                    @Override
                    public void onFailure(Call<RaspiResponse> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "한번 더 누르면 로그아웃 됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ctl = false; // 스레드 종료
            finish();
            toast.cancel();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ctl = true;
    }
}