package com.example.MobleSweetHome.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.LoginData;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordUpdateActivity extends AppCompatActivity {

    EditText pw, pw_check;
    TextView tv;
    Button btn_update, btn_home;
    RetrofitService rs = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwupdate);

        setting();
        pw_check(); // 비밀번호 체크 메소드

        btn_update.setOnClickListener(UPDATE);
        btn_home.setOnClickListener(HOME);
    }

    public void setting(){
        pw = (EditText)findViewById(R.id.update_pw);
        pw_check = (EditText)findViewById(R.id.check_pw);
        tv = (TextView)findViewById(R.id.tv_pw);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_home = (Button)findViewById(R.id.btn_home);
    }

    public void pw_check() { // 비밀번호 체크 메소드

        pw_check.addTextChangedListener(new TextWatcher() {
            // 입력하기전
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv.setText("비밀번호를 입력하세요.");
            }
            // 입력시
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pw.getText().toString().equals(pw_check.getText().toString())) {
                    tv.setText("비밀번호가 일치합니다.");
                    tv.setTextColor(Color.parseColor("#000000"));
                    btn_update.setTextColor(Color.parseColor("#000000"));
                    btn_update.setEnabled(true);
                }
                else {
                    tv.setText("비밀번호가 일치하지 않습니다.");
                    tv.setTextColor(Color.parseColor("#FF0000"));
                    btn_update.setTextColor(Color.parseColor("#808080"));
                    btn_update.setEnabled(false);
                }
            }
            // 입력이 끝났을 때
            @Override
            public void afterTextChanged(Editable editable) {
                if(pw.getText().toString().equals(pw_check.getText().toString())) {
                    tv.setText("비밀번호가 일치합니다.");
                    tv.setTextColor(Color.parseColor("#000000"));
                    btn_update.setTextColor(Color.parseColor("#000000"));
                    btn_update.setEnabled(true);
                } else {
                    tv.setText("비밀번호가 일치하지 않습니다..");
                    tv.setTextColor(Color.parseColor("#FF0000"));
                    btn_update.setTextColor(Color.parseColor("#808080"));
                    btn_update.setEnabled(false);
                }
            }
        }); // 비밀번호 체크 메소드
    }

    View.OnClickListener UPDATE = new View.OnClickListener() { // 비밀번호 변경
        @Override
        public void onClick(View view) {

            Intent intent = getIntent();
            String id = intent.getStringExtra("id");

            if(pw.length() == 0 || pw_check.length() == 0 || tv.getText().equals("비밀번호가 일치하지않습니다.") || tv.getText().equals("비밀번호를 입력하세요.")) {
                Toast.makeText(getApplicationContext(), "비밀번호 확인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                rs.service.Update_pwFunc(new LoginData(id, pw.getText().toString())).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.v(rs.TAG, "[PasswordUpdateActivity] result = " + result);
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.v(rs.TAG, "[PasswordUpdateActivity] error = " + String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(rs.TAG, "[PasswordUpdateActivity] Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    View.OnClickListener HOME = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(PasswordUpdateActivity.this, LoginActivity.class));
        }
    };
}