package com.PACOsoft.promise_betting.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.PACOsoft.promise_betting.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password extends Activity {

    private TextInputEditText et_email;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
    }

    //바깥영역 터치방지
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return false;
    }

    //뒤로가기 비활성
    @Override
    public void onBackPressed() {
        return;
    }

    //닫기 버튼
    public void btn_reset_password_page_close(View view) {
        finish();
    }

    //비밀번호 변경 버튼
    public void btn_reset_password(View view) {
        et_email = findViewById(R.id.et_email);
        if(et_email.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "이메일(ID)를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //이메일 전달 및 전송 코드
        mAuth.sendPasswordResetEmail(et_email.getText().toString());
        Toast.makeText(getApplicationContext(), "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}