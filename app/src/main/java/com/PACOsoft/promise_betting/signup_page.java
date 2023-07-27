package com.PACOsoft.promise_betting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Array;

public class signup_page extends AppCompatActivity {

    private TextInputEditText et_nick, et_id, et_pw, et_check;
    private TextInputLayout lo_nick, lo_id, lo_pw, lo_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        et_nick = findViewById(R.id.et_nickname);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_check = findViewById(R.id.et_pw_check);
        lo_nick = findViewById(R.id.lo_nickname);
        lo_id = findViewById(R.id.lo_id);
        lo_pw = findViewById(R.id.lo_pw);
        lo_check = findViewById(R.id.lo_pw_check);

    }

    public void btn_signup_close(View view) {
        finish();
    }

    public void btn_signup(View view){
        final String nickname = et_nick.getText().toString();
        final String id = et_id.getText().toString();
        final String pw = et_pw.getText().toString();
        final String check = et_check.getText().toString();
        boolean[] booleans = {false, false, false, false};

        if(nickname.isEmpty()){
            lo_nick.setError("닉네임을 입력해 주세요.");
            booleans[0] = false;
        }
        else{
            lo_nick.setError("");
            booleans[0] = true;
        }

        if(id.isEmpty()){
            lo_id.setError("아이디를 입력해 주세요.");
            booleans[1] = false;
        }
        else{
            lo_id.setError("");
            booleans[1] = true;
        }

        if(pw.isEmpty()){
            lo_pw.setError("비밀번호를 입력해 주세요.");
            booleans[2] = false;
        }
        else{
            lo_pw.setError("");
            booleans[2] = true;
        }

        if(check.isEmpty()){
            lo_check.setError("비밀번호를 한 번 더 입력해 주세요.");
            booleans[3] = false;
        }
        else if(!check.equals(pw)){
            lo_check.setError("비밀번호가 일치하지 않습니다.");
            booleans[3] = false;
        }
        else{
            lo_check.setError("");
            booleans[3] = true;
        }
    }
}