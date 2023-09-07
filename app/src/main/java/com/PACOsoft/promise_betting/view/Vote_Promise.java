package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Vote_Promise extends Dialog {
    /*
    * todo
    * 1. DB에서 값 가져와서 vote_progress에 넣어주기
    * 2. 찬성 누르면 내 정보 1로 set, 방 정보는 +1
    * 3. 반대 누르면 내 정보 2로 set, 방 정보는 그대로
    *
    * */
    private Button btn_for, btn_against;
    private TextView vote_progress;

    public Vote_Promise(@NonNull Context context, String rid, String UID) {
        super(context);
        setContentView(R.layout.activity_vote_promise);


        vote_progress = findViewById(R.id.vote_progress);
        FirebaseDatabase database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        DatabaseReference databaseReference = database.getReference("Promise").child(rid);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        ValueEventListener checkVoteValueEventListener = new ValueEventListener() {//계속 데이터 베이스 체크 하면서 vote_progress 갱신
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Promise promise = snapshot.getValue(Promise.class);
                String vote = String.valueOf(promise.getVote());
                String player = String.valueOf(promise.getNumOfPlayer());
                String tempSTR = vote+" / "+player;
                vote_progress.setText(String.valueOf(tempSTR));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addValueEventListener(checkVoteValueEventListener);





        btn_for = findViewById(R.id.id_vote_for);
        btn_against = findViewById(R.id.id_vote_against);

        btn_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DatabaseReference databaseReference2 = database.getReference("Promise").child(rid);
//                ValueEventListener setForVoteValueEventListener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Promise promise = snapshot.getValue(Promise.class);
//                        int vote = promise.getVote();
//                        int player = promise.getNumOfPlayer();
//
//
//                        //String vote = String.valueOf(promise.getVote());
//                        //String player = String.valueOf(promise.getNumOfPlayer());
//                        String tempSTR = vote+" / "+player;
//                        vote_progress.setText(String.valueOf(tempSTR));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                };
//
//                databaseReference2.addListenerForSingleValueEvent(setForVoteValueEventListener);
                dismiss();
            }
        });

        btn_against.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
}