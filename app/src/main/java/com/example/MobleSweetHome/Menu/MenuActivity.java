package com.example.MobleSweetHome.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.LoginData;
import com.example.MobleSweetHome.Data.RaspiData;
import com.example.MobleSweetHome.Data.RaspiResponse;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    RetrofitService rs = new RetrofitService();
    Button lightbtn, firebtn, securebtn, logout;
    TextView user, temper, humid, pm10, pm25, temp_state, humi_state, pm10_state, pm25_state, internal_state;
    String userid;
    Boolean ctl = true;

    private long backKeyPressedTime = 0; // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private Toast toast; // 첫 번째 뒤로가기 버튼을 누를 때 표시

    // 명균 code
    final Bundle bundle = new Bundle();
    TextView outtem, outhum, outparticul,outaddress,outhyperparticul,outweather;
    String result, Urlsum , particul,hyperparticul,hum,tem,encode1,encode2,getWeather,URL1;
    String weather = "날씨";
    String good = "좋음";
    String bad = "나쁨";
    String toobad = "매우나쁨";
    String normal = "보통";
    ImageView weathericon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setting();

        lightbtn.setOnClickListener(this);
        securebtn.setOnClickListener(this);
        securebtn.setOnClickListener(this);
        firebtn.setOnClickListener(this);
        logout.setOnClickListener(this);

        Intent intent = getIntent();
        user.setText(intent.getStringExtra("user"));

        Internalinfo(); // 온습도, 미세먼지 내부정보(Raspberry Pi Sensor)
        outinfo_server(); // 명균서버
    }

    public void setting() {
        lightbtn = (Button) findViewById(R.id.btn_lightctl);
        securebtn = (Button)findViewById(R.id.btn_securectl);
        firebtn = (Button)findViewById(R.id.btn_fire);
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

        // 명균 setting
        outtem =  findViewById(R.id.tv_out_temper);
        outhum =  findViewById(R.id.tv_out_humid);
        outaddress =  findViewById(R.id.tv_address);
        outparticul =  findViewById(R.id.tv_out_particul);
        outhyperparticul = findViewById(R.id.tv_out_hyperparticul);
        outweather =findViewById(R.id.tv_weather);
        weathericon = findViewById(R.id.weatherIcon);
    }


    public void Internalinfo() {
        Thread thread = new Thread("temp & humi Thread") {

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
                        Thread.sleep(10000); // 10초에 한번씩 내부정보가 갱신되도록
                   } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    } // 온습도, 미세먼지 정보

    //hanler을 이용하여 키값 찾아서 번들데이터 텍스트뷰에 넣기
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            outtem.setText(bundle.getString("temperature"));
            outhum.setText(bundle.getString("humid"));
            outparticul.setText(bundle.getString("particul"));
            outhyperparticul.setText(bundle.getString("hyperparticul"));
            outweather.setText(bundle.getString("weather"));
            if (particul.equals(good)){
                outparticul.setTextColor(Color.parseColor("#32a1ff"));
            }else if (particul.equals(normal)){
                outparticul.setTextColor(Color.parseColor("#00c73c"));
            }else if (particul.equals(bad)) {
                outparticul.setTextColor(Color.parseColor("#fd9b5a"));
            }else if (particul.equals(toobad)){
                outparticul.setTextColor(Color.parseColor("#fd5959"));}

            if (hyperparticul.equals(good)){
                outhyperparticul.setTextColor(Color.parseColor("#32a1ff"));
            }else if (hyperparticul.equals(normal)){
                outhyperparticul.setTextColor(Color.parseColor("#00c73c"));
            }else if (hyperparticul.equals(bad)) {
                outhyperparticul.setTextColor(Color.parseColor("#fd9b5a"));
            }else if (hyperparticul.equals(toobad)){
                outhyperparticul.setTextColor(Color.parseColor("#fd5959"));}

            if (bundle.getString("weather").equals("비")) {
                weathericon.setImageResource(R.drawable.rain);
            }else if (bundle.getString("weather").equals("흐림")){
                weathericon.setImageResource(R.drawable.cloudy);
            }else if (bundle.getString("weather").equals("구름조금")){
                weathericon.setImageResource(R.drawable.alittlecloud);
            }else if (bundle.getString("weather").equals("구름많음")){
                weathericon.setImageResource(R.drawable.cloudiness);
            }else if (bundle.getString("weather").equals("약한비")){
                weathericon.setImageResource(R.drawable.lightrain);
            }else if (bundle.getString("weather").equals("강한비")){
                weathericon.setImageResource(R.drawable.heavyrain);
            }else if (bundle.getString("weather").equals("약한눈")){
                weathericon.setImageResource(R.drawable.lightsnow);
            }else if (bundle.getString("weather").equals("눈")){
                weathericon.setImageResource(R.drawable.snow);
            }else if (bundle.getString("weather").equals("강한눈")){
                weathericon.setImageResource(R.drawable.heavysnow);
            }else if (bundle.getString("weather").equals("흐린 후 갬")){
                weathericon.setImageResource(R.drawable.cloudyandsunny);
            }else if (bundle.getString("weather").equals("비 후 갬")){
                weathericon.setImageResource(R.drawable.rainandsunny);
            }else if (bundle.getString("weather").equals("눈 후 갬")){
                weathericon.setImageResource(R.drawable.snowandsunny);
            }else if (bundle.getString("weather").equals("흐려져 비")){
                weathericon.setImageResource(R.drawable.sunnyandrain);
            }else if (bundle.getString("weather").equals("흐려져 눈")){
                weathericon.setImageResource(R.drawable.sunnyandsnow);
            }else if (bundle.getString("weather").equals("맑음")){
                weathericon.setImageResource(R.drawable.sunny);
            }
        }
    };


    public void outinfo_server() {
        Call<ResponseBody> call_post = rs.service.userId_Func(new LoginData(user.getText().toString()));
        call_post.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    try {
                        result = response.body().string();

                        outaddress.setText(result);
                        Log.v(rs.TAG, "result = " + result);
                        encode1 = URLEncoder.encode(result,"UTF-8");
                        Log.d(rs.TAG, "encode1 = " + encode1);
//                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        outinfoThred();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    } // 사용자 아이디를 서버 측에 보내서 DB에 해당 아이디가 존재하면 가입한 당시 주소를 이용해 외부정보 웹 크롤링

    public void outinfoThred ( ) {
        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try {
                    encode2 = URLEncoder.encode( weather,"UTF-8");
                    URL1 = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=";
                    Urlsum = URL1.concat(encode1).concat(encode2);

                    Document document = Jsoup.connect(Urlsum).get();
                    Elements elements1 = document.select(".weather_info").select("strong");
                    Elements elements2 = document.select(".temperature_info").select("dd[class=desc]").next().next();
                    Elements elements3 = document.select(".today_chart_list").select(".txt").select("span");
                    Elements elements4 = document.select(".weather_main");

                    Log.d("rs.TAG","초먼지 : "+elements3);
                    boolean isEmpty = elements1.isEmpty();

                    Log.d("Tag", "isNull? : " + isEmpty);
                    if (isEmpty == false) {
                        tem = elements1.get(0).text().substring(5);
                        hum = elements2.get(0).text();
                        particul = elements3.get(0).text();
                        hyperparticul = elements3.get(1).text();
                        getWeather = elements4.get(0).text();

                        bundle.putString("temperature", tem);
                        bundle.putString("humid", hum);
                        bundle.putString("particul",particul);
                        bundle.putString("hyperparticul",hyperparticul);
                        bundle.putString("weather",getWeather);

                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    } // 외부정보 가져오기

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lightctl: // 조명제어
                Intent light =new Intent(getApplicationContext(), LightActivity.class);
                startActivity(light);
                break;
            case R.id.btn_securectl: // 보안 시스템
                Intent secure = new Intent(getApplicationContext(), SecureActivity.class);
                startActivity(secure);
                break;
            case R.id.btn_fire: // 화재 시스템
                Intent fire = new Intent(getApplicationContext(), FireActivity.class);
                startActivity(fire);
                break;
            case R.id.btn_logout: // 로그아웃
                ctl = false; // 스레드 종료하게끔 true -> false
                Toast.makeText(getApplicationContext(), "로그아웃이 되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 액티비티 종료, 추후 서버 연동 고려?
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
            ctl = false; // 스레드 종료하게끔 true -> false
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