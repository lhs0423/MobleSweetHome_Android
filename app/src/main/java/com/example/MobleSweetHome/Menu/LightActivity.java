package com.example.MobleSweetHome.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.MobleSweetHome.Data.RaspiData;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LightActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    RetrofitService rs = new RetrofitService();

    Button menubtn;
    Switch light1,light2,light3;
    ImageView imageView1,imageView2,imageView3;
    TextView tv_sensor_state;
    Boolean ctl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        ctl = true;

        setting();

        light1.setOnCheckedChangeListener(this);
        light2.setOnCheckedChangeListener(this);
        light3.setOnCheckedChangeListener(this);
        menubtn.setOnClickListener(MENU);

        led_check();
    }

    public void setting(){
        menubtn = (Button)findViewById(R.id.lightctl_menu);
        light1 = (Switch)findViewById(R.id.switch_light1);
        light2 = (Switch)findViewById(R.id.switch_light2);
        light3 =( Switch)findViewById(R.id.switch_light3);
        imageView1=(ImageView)findViewById(R.id.light1);
        imageView2=(ImageView)findViewById(R.id.light2);
        imageView3=(ImageView)findViewById(R.id.light3);
        tv_sensor_state = (TextView)findViewById(R.id.tv_sensor_state);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ctl = false;
    }

    public void led_check() { // LED on/off 상태를 반복적으로 체크해서 반영하는 메서드
        Thread thread = new Thread("Led Check Thread") {

            @Override
            public void run() {
                super.run();

                while(ctl) {
                    try {
                        rs.service.Led_Check1(new RaspiData()).enqueue(new Callback<RaspiData>() { // 거실 LED
                            @Override
                            public void onResponse(Call<RaspiData> call, Response<RaspiData> response) {
                                RaspiData result = response.body();
                                String check = String.valueOf(result.getCheck());
                                if(check.equals("1")) light1.setChecked(true); // on
                                else if(check.equals("0")) light1.setChecked(false); // off
                                else if(check.equals("-1")) {
                                    light1.setChecked(false);
                                    tv_sensor_state.setText("* 센서 접속 실패");
                                }
                            }
                            @Override
                            public void onFailure(Call<RaspiData> call, Throwable t) {
                                light1.setChecked(false);
                                tv_sensor_state.setText("* 센서 접속 실패");
                            }
                        });

                        rs.service.Led_Check2(new RaspiData()).enqueue(new Callback<RaspiData>() { // 화장실 LED
                            @Override
                            public void onResponse(Call<RaspiData> call, Response<RaspiData> response) {
                                RaspiData result = response.body();
                                String check = String.valueOf(result.getCheck());
                                if(check.equals("1")) light2.setChecked(true); // on
                                else if(check.equals("0")) light2.setChecked(false); // off
                                else if(check.equals("-1")) {
                                    light2.setChecked(false);
                                    tv_sensor_state.setText("* 센서 접속 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<RaspiData> call, Throwable t) {
                                light2.setChecked(false);
                                tv_sensor_state.setText("* 센서 접속 실패");
                            }
                        });

                        rs.service.Led_Check3(new RaspiData()).enqueue(new Callback<RaspiData>() { // 방1 LED
                            @Override
                            public void onResponse(Call<RaspiData> call, Response<RaspiData> response) {
                                RaspiData result = response.body();
                                String check = String.valueOf(result.getCheck());
                                if(check.equals("1")) light3.setChecked(true); // on
                                else if(check.equals("0")) light3.setChecked(false); // off
                                else if(check.equals("-1")) {
                                    light3.setChecked(false);
                                    tv_sensor_state.setText("* 센서 접속 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<RaspiData> call, Throwable t) {
                                light3.setChecked(false);
                                tv_sensor_state.setText("* 센서 접속 실패");
                            }
                        });
                        Thread.sleep(5000); // 5초에 한번씩 갱신할 수 있도록
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    } // LED on/off 상태를 반복적으로 체크해서 LED 상태를 반영하는 메서드

    View.OnClickListener MENU = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LightActivity.this, MenuActivity.class);
            setResult(RESULT_OK, intent);
            ctl = false; // 스레드 종료하게끔 true -> false
            finish(); // 해당 액티비티 종료
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_light1: // 거실 LED
                if(isChecked) imageView1.setImageResource(R.drawable.turnon1);
                else imageView1.setImageResource(R.drawable.turnoff1);
                rs.service.Room1_Func(new RaspiData(isChecked)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                if(result.equals("실패")) tv_sensor_state.setText("* 센서 접속 실패"); // Raspberrypi Flask Server Fail
                                else tv_sensor_state.setText(""); // Raspberrypi Flask Server Success, LED ON(1) or OFF(0)
                                Log.v(rs.TAG, "[LightActivity] LED1 : " + result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(rs.TAG, "[LightActivity] LED1 : Fail");
                    }
                });
                break;

            case R.id.switch_light2: // 화장실 LED
                if(isChecked) imageView2.setImageResource(R.drawable.turnon1);
                else imageView2.setImageResource(R.drawable.turnoff1);
                rs.service.Room2_Func(new RaspiData(isChecked)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            if(result.equals("실패")) tv_sensor_state.setText("* 센서 접속 실패"); // Raspberrypi Flask Server Fail
                            else tv_sensor_state.setText(""); // Raspberrypi Flask Server Success, LED ON(1) or OFF(0)
                            Log.v(rs.TAG, "[LightActivity] : " + result);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(rs.TAG, "[LightActivity] : Fail");
                    }
                });
                break;

            case R.id.switch_light3: // 방1 LED
                if(isChecked) imageView3.setImageResource(R.drawable.turnon1);
                else imageView3.setImageResource(R.drawable.turnoff1);
                rs.service.Room3_Func(new RaspiData(isChecked)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            if(result.equals("실패")) tv_sensor_state.setText("* 센서 접속 실패"); // Raspberrypi Flask Server Fail
                            else tv_sensor_state.setText(""); // Raspberrypi Flask Server Success, LED ON(1) or OFF(0)
                            Log.v(rs.TAG, "[LightActivity] : " + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(rs.TAG, "[LightActivity] : Fail");
                    }
                });
                break;
            default:
                break;
        }
    }
}