package com.example.MobleSweetHome.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public int gas_data, tem_data;

    Handler handler = new Handler();

    String stgas,sttemp;
    Boolean ctl = true;

    final Bundle bundle = new Bundle();
//    TextView tv_token, tv_gas;
//    Button btn_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

        setting();
        gastemp_Data();





//        if(gas_data >= 200 || tem_data >= 20) {
//            calling.setEnabled(true);
//        }

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

    public void gastemp_Data () {
        Thread thread = new Thread() {
            public void run() {
                super.run();
                while (ctl) {
                    //가스
                    rs.service.Gas_Func(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                        @Override
                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                            RaspiResponse result = response.body();
                            gas_data = result.getGas(); // 가스 데이터
                            stgas = String.valueOf(gas_data);
                            gas.setText(stgas);

                            if (gas_data > 300) {
                                gas.setTextColor(Color.parseColor("#fd5959"));
                                gasimage.setImageResource(R.drawable.red_light);
//                                calling.setEnabled(true);

                            }else {
                                gas.setTextColor(Color.parseColor("#00c73c"));
                                gasimage.setImageResource(R.drawable.green_light);
//                                calling.setEnabled(false);
                            }
                        }
                        @Override
                        public void onFailure(Call<RaspiResponse> call, Throwable t) {

                        }
                    });
                    // 온도
                    rs.service.Temp_Humi(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                        @Override
                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                            RaspiResponse result = response.body();
                            tem_data = result.getTemp(); // 온도 데이터
                            sttemp = String.valueOf(tem_data);
                            tem.setText(sttemp);

                            if (tem_data > 50 ) {
                                tem.setTextColor(Color.parseColor("#fd5959"));
                                temperimage.setImageResource(R.drawable.red_light);
//                                calling.setEnabled(true);
                            }else {
                                tem.setTextColor(Color.parseColor("#00c73c"));
                                temperimage.setImageResource(R.drawable.green_light);
//                                calling.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<RaspiResponse> call, Throwable t) {

                        }
                    });

                    Log.v("Log" , gas_data + " , " + tem_data); // 여기까지는 데이터 확인 가능


//                    if(gas_data >= 200 || tem_data >= 20) {
//                        Message msg = btn_red_change.obtainMessage();
//                        btn_red_change.sendMessage(msg);
//                    }
//                    else calling.setEnabled(false);



//                    if (temdata > 20) {
//                        gas.setTextColor(Color.parseColor("#fd5959"));
//                        gasimage.setImageResource(R.drawable.red_light);
//                        tem.setTextColor(Color.parseColor("#fd5959"));
//                        temperimage.setImageResource(R.drawable.red_light);
//                        calling.setEnabled(true);
//                    } else if(gasdata > 200) {
//                        gas.setTextColor(Color.parseColor("#00c73c"));
//                        gasimage.setImageResource(R.drawable.green_light);
//                        tem.setTextColor(Color.parseColor("#00c73c"));
//                        temperimage.setImageResource(R.drawable.green_light);
//                        calling.setEnabled(false);
//                    }
//                    change();
                    try {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(gas_data >= 300 || tem_data >= 50) calling.setEnabled(true);
                                else calling.setEnabled(false);
                            }
                        });


//                        if(gas_data >= 200 || tem_data >= 20) {
//                            Message msg = btn_red_change.obtainMessage();
//                            btn_red_change.sendMessage(msg);
//                        }
//                        bundle.putInt("gas", gas_data);
//                        bundle.putInt("temperature",tem_data);
//                        Message msg = handler.obtainMessage();
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);

                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    calling.setEnabled(true);
                }
            }
        };
        thread.start();
    }

    public void change() {
        if (gas_data >= 200 || tem_data >= 20) {
            calling.setEnabled(true);
        } else {
            calling.setEnabled(false);
        }
    }


//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Bundle bundle = msg.getData();
//            tem.setText(bundle.getInt("temperature"));
//            gas.setText(bundle.getInt("gas"));
//
//            if(bundle.getInt("temperature") > 20 || bundle.getInt("gas") > 200) {
//                calling.setEnabled(true);
//            } else {
//                calling.setEnabled(false);
//            }
//        }
//    };

//    Handler btn_gray_change = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            calling.setEnabled(false);
//        }
//    };


//    public void temData () {
//        Thread thread =new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                while (ctl) {
//                    rs.service.Temp_Humi(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
//                        @Override
//                        public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
//                            RaspiResponse result = response.body();
//                            temdata = Integer.valueOf(result.getTemp());
//                            sttemp = String.valueOf(temdata);
//
//                            tem.setText(sttemp);
//                            if (temdata >50 ) {
//                                tem.setTextColor(Color.parseColor("#fd5959"));
//                                temperimage.setImageResource(R.drawable.red_light);
//                                calling.setEnabled(true);
//                            }else {
//                                tem.setTextColor(Color.parseColor("#00c73c"));
//                                temperimage.setImageResource(R.drawable.green_light);
//                                calling.setEnabled(false);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<RaspiResponse> call, Throwable t) {
//
//                        }
//                    });
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        thread.start();
//    }

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