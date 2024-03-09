package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class Coin extends Activity {
    private int tempcoin;
    private String UID;
    private int originCoin;
    private TextInputEditText coin_tv;
    private TextView after_coin_tv;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID"); //Home에서 intent해준 UID를 받아옴
        coin_tv = findViewById(R.id.et_coin);
        after_coin_tv = findViewById(R.id.tv_afterCoin);
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        //user = new User();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DataSnapshot) {
                    User me = DataSnapshot.getValue(User.class);
                    //나의 account 불러와서 originCoin에 넣어줌
                    originCoin = me.getAccount();
                    after_coin_tv.setText(String.valueOf(originCoin+5000));

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        coin_tv.addTextChangedListener(new TextWatcher() {
            int charge_coin;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                charge_coin = 0;
//                if(0 < coin_tv.getText().length() && coin_tv.getText().length() < 11) {
//                    charge_coin += Integer.valueOf(coin_tv.getText().toString());
//                    after_coin_tv.setText(String.valueOf(charge_coin + originCoin));
//                }
//                else if(coin_tv.getText().length() <= 0){
//                    after_coin_tv.setText(String.valueOf(originCoin));
//                }

//                after_coin_tv.setText(String.valueOf(originCoin + 5000));
            }
        });
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

    //닫기버튼
    public void btn_coin_page_close(View view) {
        finish();
    }

    public void btn_coin_charge(View view) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DataSnapshot) {
                User me = DataSnapshot.getValue(User.class);
                String recentAttend = me.getAttendDate();
                String[] attendDate = recentAttend.split(" ");
                int DByyyy = Integer.parseInt(attendDate[0].toString());
                int DBMM = Integer.parseInt(attendDate[1].toString());
                int DBdd = Integer.parseInt(attendDate[2].toString());
                int nowyyyy = Integer.parseInt(dateFormat("yyyy"));
                int nowMM = Integer.parseInt(dateFormat("MM"));
                int nowdd = Integer.parseInt(dateFormat("dd"));
                if(nowyyyy != DByyyy || nowMM != DBMM || nowdd != DBdd)
                {
                    databaseReference.child("isAttend").setValue(false);
                }
                if(nowyyyy == DByyyy&& nowMM == DBMM && nowdd == DBdd && me.getIsAttend() == true)
                {
                    Toast.makeText(getApplicationContext(), "오늘은 이미 코인을 충전하셨습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    databaseReference.child("account").setValue(me.getAccount() + 5000);//더해준 최종 값 DB에 추가
                    databaseReference.child("attendDate").setValue(nowyyyy+" "+nowMM+" "+nowdd);
                    databaseReference.child("isAttend").setValue(true);
                    Toast.makeText(getApplicationContext(), "충전 완료", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        finish();
    }

    public String dateFormat(String pattern) {
        Date date = new Date();
        return new SimpleDateFormat(pattern).format(date);
    }
}