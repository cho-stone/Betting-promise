package com.PACOsoft.promise_betting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.collection.LLRBNode;

import java.sql.Array;
import java.util.regex.Pattern;

public class signup_page extends AppCompatActivity {
    private final String nick_validation = "^[a-z가-힇]+[a-z0-9가-힇]{1,10}$";
    private final String id_validation = "^[a-z]+[a-z0-9]{5,12}$";
    private final String pw_validation = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
    private TextInputEditText et_nick, et_id, et_pw, et_check;
    private TextInputLayout lo_nick, lo_id, lo_pw, lo_check;
    private TextView dupli_tv;

    private boolean[] booleans = {false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        dupli_tv = findViewById(R.id.dupli_tv);
        et_nick = findViewById(R.id.et_nickname);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_check = findViewById(R.id.et_pw_check);
        lo_nick = findViewById(R.id.lo_nickname);
        lo_id = findViewById(R.id.lo_id);
        lo_pw = findViewById(R.id.lo_pw);
        lo_check = findViewById(R.id.lo_pw_check);

        et_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String nickCheck = et_nick.getText().toString();
                boolean validation = Pattern.matches(nick_validation, nickCheck);
                if(validation){
                    lo_nick.setError("");
                    booleans[0] = true;
                }
                else if(nickCheck.isEmpty()){
                    lo_nick.setError("닉네임을 입력해 주세요.");
                    booleans[0] = false;
                }
                else{
                    lo_nick.setError("닉네임 형식이 올바르지 않습니다.");
                    booleans[0] = false;
                }
            }
        });

        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String idCheck = et_id.getText().toString();
                boolean validation = Pattern.matches(id_validation, idCheck);
                if(validation){
                    lo_id.setError("");
                    booleans[1] = true;
                    dupli_tv.setEnabled(true);
                }
                else if(idCheck.isEmpty()){
                    lo_id.setError("아이디를 입력해 주세요.");
                    booleans[1] = false;
                    dupli_tv.setEnabled(false);
                }
                else{
                    lo_id.setError(" * 영소문자, 숫자 조합으로 6자리 이상");
                    booleans[1] = false;
                    dupli_tv.setEnabled(false);
                }
            }
        });

        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pwCheck = et_pw.getText().toString();
                boolean validation = Pattern.matches(pw_validation, pwCheck);
                if(validation){
                    lo_pw.setError("");
                    booleans[2] = true;
                }
                else if(pwCheck.isEmpty()){
                    lo_pw.setError("비밀번호를 입력해 주세요.");
                    booleans[2] = false;
                }
                else{
                    lo_pw.setError(" * 영문자, 숫자, 특수문자(모두) 조합으로 8자리 이상");
                    booleans[2] = false;
                }
            }
        });
    }

    public void btn_dupli_check(View view){
        String id = et_id.getText().toString();
        Log.v("tt", id);
        //TODO : 중복제거 코드
    }

    public void btn_signup_close(View view) {
        finish();
    }

    public void btn_signup(View view){
        final String nickname = et_nick.getText().toString();
        final String id = et_id.getText().toString();
        final String pw = et_pw.getText().toString();
        final String check = et_check.getText().toString();

        if(nickname.isEmpty()){
            lo_nick.setError("닉네임을 입력해 주세요.");
            booleans[0] = false;
        }

        if(id.isEmpty()){
            lo_id.setError("아이디를 입력해 주세요.");
            booleans[1] = false;
        }

        if(pw.isEmpty()){
            lo_pw.setError("비밀번호를 입력해 주세요.");
            booleans[2] = false;
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