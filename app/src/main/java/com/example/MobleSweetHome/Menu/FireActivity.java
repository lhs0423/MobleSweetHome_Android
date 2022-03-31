package com.example.MobleSweetHome.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MobleSweetHome.Data.RaspiData;
import com.example.MobleSweetHome.Data.RaspiResponse;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireActivity extends AppCompatActivity {

    RetrofitService rs = new RetrofitService();
    Button returnmenu,calling;
    ImageView temperimage, gasimage;
    TextView tem,gas;
    Integer gasdata, temdata;
    String stgas,sttemp;
    Boolean ctl = true;

//    TextView tv_token, tv_gas;
//    Button btn_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

        setting();
        gasData();
        temData();

//        btn_token.setOnClickListener(Token);

//        FirebaseMessaging.getInstance().getToken() // 토큰얻기
//               .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if(!task.isSuccessful()) {
//                            System.out.println("Token Fail");
//                            return;
//                        }
//
//                        String token = task.getResult();
//
//                        System.out.println(token);
//                        Toast.makeText(FireActivity.this, "token : " + token , Toast.LENGTH_SHORT).show();
//                        tv_token.setText(token);
//                    }
//                });
    }

//    public void setting() {
//        tv_token = (TextView)findViewById(R.id.tv_token);
//        tv_gas = (TextView)findViewById(R.id.tv_gas);
//        btn_token = (Button)findViewById(R.id.btn_token);
//    }

    public void setting() {
        returnmenu = findViewById(R.id.fire_back);
        calling =findViewById(R.id.call119);
        temperimage= findViewById(R.id.iv_temper);
        gasimage=findViewById(R.id.iv_gas);
        tem=findViewById(R.id.tv_temper);
        gas=findViewById(R.id.tv_gas);
        returnmenu.setOnClickListener(returntomenu);
        calling.setOnClickListener(callto119);
    }

    View.OnClickListener returntomenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ctl= false;
            finish();
        }
    };

    View.OnClickListener callto119 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse("tel:119");
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        }
    };

    public void gasData () {
        Thread thread = new Thread() {
            public void run() {
                super.run();
                while (ctl) {
                    rs.service.Gas_Func(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                        @Override
                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                            RaspiResponse result = response.body();
                            gasdata = Integer.valueOf(result.getGas());
                            stgas = String.valueOf(gasdata);
//                gasdata = String.valueOf(result.getGas());
//                Log.d(rs.TAG ,"가스"+gasdata);
//                gas.setText(gas);
                            gas.setText(stgas);
                            if (gasdata >300) {
                                gas.setTextColor(Color.parseColor("#fd5959"));
                                gasimage.setImageResource(R.drawable.red_light);
                                calling.setEnabled(true);

                            }else {
                                gas.setTextColor(Color.parseColor("#00c73c"));
                                gasimage.setImageResource(R.drawable.green_light);
                                calling.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<RaspiResponse> call, Throwable t) {

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


    public void temData () {
        Thread thread =new Thread() {
            @Override
            public void run() {
                super.run();
                while (ctl) {
                    rs.service.Temp_Humi(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                        @Override
                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                            RaspiResponse result = response.body();
                            temdata = Integer.valueOf(result.getTemp());
                            sttemp = String.valueOf(temdata);

                            tem.setText(sttemp);
                            if (temdata >50 ) {
                                tem.setTextColor(Color.parseColor("#fd5959"));
                                temperimage.setImageResource(R.drawable.red_light);
                                calling.setEnabled(true);
                            }else {
                                tem.setTextColor(Color.parseColor("#00c73c"));
                                temperimage.setImageResource(R.drawable.green_light);
                                calling.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<RaspiResponse> call, Throwable t) {

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

//    View.OnClickListener Token = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            rs.service.Gas_Func(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
//                @Override
//                public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
//                    RaspiResponse result = response.body();
//                    String gas = String.valueOf(result.getGas());
//                    tv_gas.setText(gas);
//                }
//
//                @Override
//                public void onFailure(Call<RaspiResponse> call, Throwable t) {
//
//                }
//            });
//
//
//        }
//    };

}