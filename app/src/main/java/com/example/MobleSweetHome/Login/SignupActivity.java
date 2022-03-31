package com.example.MobleSweetHome.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MobleSweetHome.Data.SignupData;
import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.Server.RetrofitService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    RetrofitService rs = new RetrofitService();

    EditText et_id, et_pw, et_pw_check, et_name, et_email, et_domain, et_birth, et_day, et_number;
    TextView tv_check;
    Button btn_signup;
    Spinner sp_city, sp_gu, sp_month, sp_domain;
    ArrayAdapter monthAdaptor, cityAdaptor, mailAdaptor;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setting();
        initAddressSpinner(); // (시/도) 스피너 메소드
        get_domain(); // 이메일 스피너 선택시, 도메인 edittext에 반영되는 메소드
        pw_check(); // 비밀번호 체크 메소드
        btn_signup.setOnClickListener(signup); // 회원가입 버튼
    }

    public void setting(){
        et_id = (EditText)findViewById(R.id.signup_id);
        et_pw = (EditText)findViewById(R.id.signup_pw);
        et_pw_check = (EditText)findViewById(R.id.signup_check_pw);
        et_name = (EditText)findViewById(R.id.signup_name);
        et_birth = (EditText)findViewById(R.id.signup_birth);
        et_day = (EditText)findViewById(R.id.signup_day);
        et_number = (EditText)findViewById(R.id.signup_phonenum);
        et_email = (EditText)findViewById(R.id.signup_email);
        et_domain = (EditText)findViewById(R.id.signup_domain);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        tv_check = (TextView)findViewById(R.id.regi_tv_compare); // 비밀번호 확인 텍스트
        sp_domain = (Spinner)findViewById(R.id.spinner_mail);
        sp_month = (Spinner) findViewById(R.id.spinner_month);
        sp_city = (Spinner)findViewById(R.id.spinner_si);
        sp_gu = (Spinner)findViewById(R.id.spinner_gu);

        mailAdaptor = ArrayAdapter.createFromResource(this,R.array.email_choice, android.R.layout.simple_spinner_dropdown_item);
        sp_domain.setAdapter(mailAdaptor);

        cityAdaptor = ArrayAdapter.createFromResource(this,R.array.country_area, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sp_city.setAdapter(cityAdaptor);

        monthAdaptor = ArrayAdapter.createFromResource(this,R.array.date_month, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sp_month.setAdapter(monthAdaptor);
    }

    private void initAddressSpinner() {
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 시군구, 동의 스피너를 초기화한다.
                switch (position) {
                    case 0:
                        setguSpinnerAdapterItem(R.array.select_gu);
                        break;
                    case 1:
                        setguSpinnerAdapterItem(R.array.seoul_area);
                        break;
                    case 2:
                        setguSpinnerAdapterItem(R.array.incheon_area);
                        break;
                    case 3:
                        setguSpinnerAdapterItem(R.array.deajeon_area);
                        break;
                    case 4:
                        setguSpinnerAdapterItem(R.array.gwangju_area);
                        break;
                    case 5:
                        setguSpinnerAdapterItem(R.array.daegu_area);
                        break;
                    case 6:
                        setguSpinnerAdapterItem(R.array.ulsan_area);
                        break;
                    case 7:
                        setguSpinnerAdapterItem(R.array.busan_area);
                        break;
                    case 8:
                        setguSpinnerAdapterItem(R.array.gyeonggi_area);
                        break;
                    case 9:
                        setguSpinnerAdapterItem(R.array.gangwon_area);
                        break;
                    case 10:
                        setguSpinnerAdapterItem(R.array.chungcheongbuk_area);
                        break;
                    case 11:
                        setguSpinnerAdapterItem(R.array.chungcheongnam_area);
                        break;
                    case 12:
                        setguSpinnerAdapterItem(R.array.jeollabuk_area);
                        break;
                    case 13:
                        setguSpinnerAdapterItem(R.array.jeollanam_area);
                        break;
                    case 14:
                        setguSpinnerAdapterItem(R.array.gyeongsangbuk_area);
                        break;
                    case 15:
                        setguSpinnerAdapterItem(R.array.gyeongsangnam_area);
                        break;
                    case 16:
                        setguSpinnerAdapterItem(R.array.jeju_area);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    } // (시/도) 스피너 메소드

    private void setguSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            sp_gu.setAdapter(null);
            arrayAdapter = null;
        }

        if (sp_city.getSelectedItemPosition() > 1) {

        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gu.setAdapter(arrayAdapter);
    } // 구 스피너 메소드

    public void get_domain() {
        sp_domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    et_domain.setText("");
                    et_domain.setHint("직접입력");
                }else if (position != 0) {
                    et_domain.setText(adapterView.getItemAtPosition(position).toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    } // 이메일 스피너 선택시, 도메인 edittext에 반영되는 메소드

    public void pw_check() { // 비밀번호 체크 메서드

        et_pw_check.addTextChangedListener(new TextWatcher() {
            //입력하기전
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_check.setText("비밀번호를 입력하세요");
            }
            //입력시
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_pw.getText().toString().equals(et_pw_check.getText().toString())){
                    tv_check.setText("비밀번호가 일치합니다.");
                    tv_check.setTextColor(Color.parseColor("#000000"));
                    btn_signup.setTextColor(Color.parseColor("#000000"));
                    btn_signup.setEnabled(true);
                }
                else {
                    tv_check.setText("비밀번호가 일치하지 않습니다.");
                    tv_check.setTextColor(Color.parseColor("#FF0000"));
                    btn_signup.setTextColor(Color.parseColor("#808080"));
                    btn_signup.setEnabled(false);
                }
            }
            //입력이 끝났을때
            @Override
            public void afterTextChanged(Editable editable) {
                if (et_pw.getText().toString().equals(et_pw_check.getText().toString())){
                    tv_check.setText("비밀번호가 일치합니다.");
                    tv_check.setTextColor(Color.parseColor("#000000"));
                    btn_signup.setTextColor(Color.parseColor("#000000"));
                    btn_signup.setEnabled(true);
                }
                else {
                    tv_check.setText("비밀번호가 일치하지 않습니다.");
                    tv_check.setTextColor(Color.parseColor("#FF0000"));
                    btn_signup.setTextColor(Color.parseColor("#808080"));
                    btn_signup.setEnabled(false);
                }
            }
        });
    } // 비밀번호 체크 메소드


    View.OnClickListener signup = new View.OnClickListener() { // 회원가입 버튼 클릭
        @Override
        public void onClick(View view) {

            String id = et_id.getText().toString();
            String pw = et_pw.getText().toString();
            String name = et_name.getText().toString();
            String email = et_email.getText().toString() + "/@" + et_domain.getText().toString();
            String address = sp_gu.getSelectedItem().toString();
            String birth = et_birth.getText().toString() + "-" +sp_month.getSelectedItem().toString() + "-" + et_day.getText().toString(); // 수정
            String number = et_number.getText().toString();

            if(id.length() == 0 || pw.length() == 0 || name.length() == 0 || birth.length() == 0 || number.length() == 0 || address.length() == 0 || email.length() == 0){
                Toast.makeText(getApplicationContext(), "미기재한 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else { // 회원가입 성공 DB저장
                rs.service.SignupFunc(new SignupData(id, pw, name, birth, number, address, email)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) { // 서버통신 성공
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
                        } else { // 서버통신 에러
                            Log.v(rs.TAG, "error = " + String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "[SignupActivity] error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) { // 서버통신 실패
                        Log.v(rs.TAG, "[SignupActivity] Fail");
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}