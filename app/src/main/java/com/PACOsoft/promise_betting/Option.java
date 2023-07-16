package com.PACOsoft.promise_betting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Option extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
    }

    //로그아웃하면 MainActivity로 이동하는 버튼 구현
    public void btnLogOutClicked(View view) {
        //로그아웃 버튼 누르면 기존에 실행되던 엑티비티 모두 종료하고 로그인 화면 엑티비티로 이동
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    public void btnExitClicked(View view) {
        //앱 종료 버튼 누르면 모든 엑티비티 종류 후 앱 종료
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }



}