package com.example.MobleSweetHome.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.SearchData;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    RetrofitService rs = new RetrofitService();

    EditText et_email, et_id, et_name;
    Button search_btn_id, search_btn_pw, search_btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setting();
        search_btn_id.setOnClickListener(this);
        search_btn_pw.setOnClickListener(this);
        search_btn_home.setOnClickListener(this);
    }

    public void setting() {
        et_email = (EditText) findViewById(R.id.search_email);
        et_id = (EditText) findViewById(R.id.search_id);
        et_name = (EditText) findViewById(R.id.search_name);
        search_btn_id = (Button) findViewById(R.id.btn_search_id);
        search_btn_pw = (Button) findViewById(R.id.btn_search_pw);
        search_btn_home = (Button) findViewById(R.id.btn_search_home);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_id: // id 찾기

                String email = et_email.getText().toString();

                rs.service.Search_idFunc(new SearchData(email)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) { // 서버통신 성공
                            try {
                                String result = response.body().string();
                                Log.v(rs.TAG, "[SearchActivity] id = " + result);

                                if (result.equals("없음"))
                                    Toast.makeText(getApplicationContext(), "가입한 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "가입한 아이디는 \'" + result + "\' 입니다.", Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else { // 서버통신 에러
                            Log.v(rs.TAG, "[SearchActivity] error = " + String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) { // 서버통신 실패
                        Log.v(rs.TAG, "[SearchActivity] Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btn_search_pw: // pw 찾기

                String id = et_id.getText().toString();
                String name = et_name.getText().toString();

                rs.service.Search_pwFunc(new SearchData(id, name)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) { // 서버통신 성공
                            try {
                                String result = response.body().string();
                                Log.v(rs.TAG, "[SearchActivity] member info = " + result);
                                if (result.equals("없음")) {
                                    Toast.makeText(getApplicationContext(), "정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "정보가 일치합니다.\n 비밀번호를 변경하세요.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SearchActivity.this, PasswordUpdateActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent); // 비밀번호 변경 액티비티로 이동
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else { // 서버통신 에러
                            Log.v(rs.TAG, "[SearchActivity] error = " + String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) { // 서버통신 실패
                        Log.v(rs.TAG, "[SearchActivity] Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btn_search_home: // 로그인화면
                Toast.makeText(getApplicationContext(), "로그인 화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SearchActivity.this, LoginActivity.class));
        }

    }
}