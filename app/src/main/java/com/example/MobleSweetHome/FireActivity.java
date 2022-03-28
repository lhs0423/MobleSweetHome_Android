package com.example.MobleSweetHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.RaspiData;
import com.example.MobleSweetHome.Data.RaspiResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireActivity extends AppCompatActivity {

    TextView tv_token, tv_gas;
    Button btn_token;

    RetrofitService rs = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

        setting();

        btn_token.setOnClickListener(Token);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()) {
                            System.out.println("Token Fail");
                            return;
                        }

                        String token = task.getResult();

                        System.out.println(token);
                        Toast.makeText(FireActivity.this, "token : " + token , Toast.LENGTH_SHORT).show();
                        tv_token.setText(token);
                    }
                });
    }

    public void setting() {
        tv_token = (TextView)findViewById(R.id.tv_token);
        tv_gas = (TextView)findViewById(R.id.tv_gas);
        btn_token = (Button)findViewById(R.id.btn_token);
    }

    View.OnClickListener Token = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rs.service.Gas_Func(new RaspiData()).enqueue(new Callback<RaspiResponse>() {
                @Override
                public void onResponse(Call<RaspiResponse> call, Response<RaspiResponse> response) {
                    RaspiResponse result = response.body();
                    String gas = String.valueOf(result.getGas());
                    tv_gas.setText(gas);
                }

                @Override
                public void onFailure(Call<RaspiResponse> call, Throwable t) {

                }
            });


        }
    };

}