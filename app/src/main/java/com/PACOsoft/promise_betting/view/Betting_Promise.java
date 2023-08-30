package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.PACOsoft.promise_betting.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Betting_Promise extends Dialog {
    private TextInputEditText et_betting_coin;
    private TextView tv_betting_max;
    private FirebaseDatabase database, database2;
    private DatabaseReference databaseReference, databaseReference2;
    private ValueEventListener bettingCoinListener, userCoinListener;
    private ArrayList<String> usersUID;
    private int min, j;

    public Betting_Promise(@NonNull Context context, String rid) {
        super(context);
        setContentView(R.layout.activity_betting_promise);

        et_betting_coin = findViewById(R.id.et_betting);
        tv_betting_max = findViewById(R.id.tv_betting_coin);
        usersUID = new ArrayList<>();

        //방에서 유저 UID를 가져옴
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Promise").child(rid).child("promisePlayer");
        bettingCoinListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HashMap<String, Object>> players = (List<HashMap<String, Object>>) snapshot.getValue();

                for(int z = 0; z < players.size(); z++){
                    String s = (String) players.get(z).get("playerUID");
                    usersUID.add(s);
                }

                //유저들의 UID로 min코인 찾기
                min = 0;
                j = 0;
                database2 = FirebaseDatabase.getInstance();
                for(int i = 0; i < players.size(); i++){
                    databaseReference2 = database2.getReference("User").child(usersUID.get(i)).child("account");
                    userCoinListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int coin = snapshot.getValue(Integer.class);
                            if(min == 0 && coin >= 100){
                                min = coin;
                            }
                            else if(min > coin && coin >= 100) {
                                min = coin;
                            }

                            //max값 세팅
                            if(j == players.size()-1 && min == 0){
                                tv_betting_max.setText("배팅 불가");
                            }
                            else if(j == players.size()-1){
                                tv_betting_max.setText(String.valueOf(min));
                            }
                            j++;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Map", String.valueOf(error.toException()));
                        }
                    };
                    databaseReference2.addListenerForSingleValueEvent(userCoinListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Map", String.valueOf(error.toException()));
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