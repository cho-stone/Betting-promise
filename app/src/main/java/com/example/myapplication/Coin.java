package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class Coin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin);
    }

    //바깥영역 터치방지
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return false;
    }

    //뒤로가기 비활성
    @Override
    public void onBackPressed(){
        return;
    }

    //닫기버튼
    public void btn_coin_page_close(View view) {
        finish();
    }

    public void btn_coin_charge(View view){
        //코인 값 전달 코드
        finish();
    }
}