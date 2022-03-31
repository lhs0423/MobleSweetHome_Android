package com.example.MobleSweetHome.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.LoginData;
import com.example.MobleSweetHome.Menu.MenuActivity;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    RetrofitService rs = new RetrofitService(); // Retrofit 객체 생성
    private EditText et_id, et_pw;
    private Button btn_login, btn_sign, btn_search;

    final static int REQUEST_CODE_SIGNUP = 1;

    private long backKeyPressedTime = 0; // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private Toast toast; // 첫 번째 뒤로가기 버튼을 누를 때 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setting();

        btn_login.setOnClickListener(this);
        btn_sign.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    public void setting() {
        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_sign = (Button)findViewById(R.id.btn_sign);
        btn_search = (Button)findViewById(R.id.btn_search);
    }

    @Override
    public void onBackPressed() {
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 millseconds = 2 seconds
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "한번 더 누르면 App이 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: // 로그인
                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();

                rs.service.LoginFunc(new LoginData(id, pw)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            try {
                                String result = response.body().string();

                                if(result.equals("없음")) { // 아이디가 존재하지 않을 때
                                    Log.v(rs.TAG, "[LoginActivity] : id & pw = " + result);
                                    Toast.makeText(getApplicationContext(), "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
                                }
                                else if(result.equals("일치")) { // 아이디와 패스워드가 일치할 때
                                    Log.v(rs.TAG, "[LoginActivity] : id & pw = " + result);
                                    Toast.makeText(getApplicationContext(), "로그인 성공! " + id + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    intent.putExtra("user", id);
                                    startActivity(intent); // MenuActivity로 이동
                                }
                                else { // 비밀번호 불일치
                                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else { // 통신 에러
                            Log.v(rs.TAG, "[LoginActivity] : error = " + String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) { // 통신 실패
                        Log.v(rs.TAG, "[LoginActivity] : Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btn_sign: // 회원가입
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGNUP); // REQUEST_CODE_SINGUP = 1
                break;

            case R.id.btn_search: // 아이디 & 비밀번호 찾기
                startActivity(new Intent(LoginActivity.this, SearchActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) { // LoginActivity 재호출
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // SignupActivity에서 회원가입이 성공했을 때 LoginActivity의 ID, PW 자동 입력
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SIGNUP){
            if(resultCode == RESULT_OK){
                et_id.setText(data.getCharSequenceExtra("signup_id"));
                et_pw.setText(data.getCharSequenceExtra("signup_pw"));
            }
        }
    }


}