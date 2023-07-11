package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        if(nickname.isEmpty()){
            lo_nick.setError("입력");
        }
        else{
            lo_nick.setError("");
        }
        if(id.isEmpty()){

        }
        else{
            lo_id.setError("");
        }
        if(pw.isEmpty()){

        }
        else{
            lo_pw.setError("");
        }
        if(check.isEmpty()){

        }
        else{
            lo_nick.setError("");
        }
    }
}