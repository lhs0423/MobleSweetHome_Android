package com.example.MobleSweetHome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.SignupData;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText et_id, et_pw, et_pw_check, et_name, et_birth, et_day, et_number, et_address, et_email;
    Button btn_signup, btn_check;
    TextView tv_check;
    RetrofitService rs = new RetrofitService();
    String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Spinner monthSpinner = (Spinner) findViewById(R.id.signup_month);
        ArrayAdapter monthAdaptor = ArrayAdapter.createFromResource(this,R.array.date_month, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdaptor);
        month = monthSpinner.getSelectedItem().toString();

        setting();

        btn_signup.setOnClickListener(signup);
        btn_check.setOnClickListener(check);
    }

    public void setting(){
        et_id = (EditText)findViewById(R.id.signup_id);
        et_pw = (EditText)findViewById(R.id.signup_pw);
        et_pw_check = (EditText)findViewById(R.id.signup_check_pw);
        et_name = (EditText)findViewById(R.id.signup_name);
        et_birth = (EditText)findViewById(R.id.signup_birth);
        et_day = (EditText)findViewById(R.id.signup_day);
        et_number = (EditText)findViewById(R.id.signup_phonenum);
        et_address = (EditText)findViewById(R.id.signup_address);
        et_email = (EditText)findViewById(R.id.signup_email);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        btn_check = (Button)findViewById(R.id.regi_btn_compare);
        tv_check = (TextView)findViewById(R.id.regi_tv_compare);
    }

    View.OnClickListener check = new View.OnClickListener() { // 비밀번호 확인
        @Override
        public void onClick(View view) {
            if (et_pw.getText().toString().equals(et_pw_check.getText().toString())){
                tv_check.setText("비밀번호가 일치합니다.");
            }
            else{
                tv_check.setText("비밀번호가 일치하지않습니다.");
            }
        }
    };

    View.OnClickListener signup = new View.OnClickListener() { // 회원가입
        @Override
        public void onClick(View view) {
            String id = et_id.getText().toString();
            String pw = et_pw.getText().toString();
            String name = et_name.getText().toString();
            String birth = et_birth.getText().toString() + "-" + month + "-" + et_day.getText().toString(); // 수정
            String number = et_number.getText().toString();
            String address = et_address.getText().toString();
            String email = et_email.getText().toString();

            if(id.length() == 0 || pw.length() == 0 || name.length() == 0 || birth.length() == 0 || number.length() == 0 || address.length() == 0 || email.length() == 0){
                Toast.makeText(getApplicationContext(), "미기재한 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else if(tv_check.getText().equals("비밀번호가 일치하지않습니다.") || tv_check.getText().equals("비밀번호를 입력하세요.")) {
                Toast.makeText(getApplicationContext(), "비밀번호 확인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                rs.service.SignupFunc(new SignupData(id, pw, name, birth, number, address, email)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.v(rs.TAG, "[SignupActivity] : " + result);
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                if(result.equals("회원가입 성공!")) {
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    intent.putExtra("signup_id", et_id.getText()); // 회원가입 하면 LoginActivity에 id, pw가 자동으로 입력될 수 있도록 데이터 전달
                                    intent.putExtra("signup_pw", et_pw.getText());
                                    setResult(RESULT_OK, intent);
                                    finish(); // 해당 액티비티 종료
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.v(rs.TAG, "error = " + String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "[SignupActivity] error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(rs.TAG, "[SignupActivity] Fail");
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}