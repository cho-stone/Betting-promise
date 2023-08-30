package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.TextView;

import com.PACOsoft.promise_betting.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class Betting_Promise extends Dialog {
    private TextInputEditText et_betting_coin;
    private TextView tv_betting_max;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ValueEventListener bettingCoinListener;

    public Betting_Promise(@NonNull Context context, String rid, int num) {
        super(context);
        setContentView(R.layout.activity_betting_promise);

        et_betting_coin = findViewById(R.id.et_betting);
        tv_betting_max = findViewById(R.id.tv_betting_coin);

        //TODO: 방에서 유저 UID를 가져오고 유저 코인을 가져 온후 가장 작은 코인을 max에 넣기
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Promise").child(rid).child("promisePlayer");
        bettingCoinListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HashMap<String, Object>> players = (List<HashMap<String, Object>>) snapshot.getValue();

                for(int i = 0; i < players.size(); i++){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(bettingCoinListener);

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
}