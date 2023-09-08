package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tv_betting_max, tv_curr_betting;
    private FirebaseDatabase database, database2;
    private DatabaseReference databaseReference, databaseReference2, mDatabase; //mDatabase는 setValue전용
    private ValueEventListener bettingCoinListener, userCoinListener, currVoteListener;
    private ArrayList<String> usersUID;
    private Button btn_betting;
    private int min, j, me_num, currBettingNum, allBettingMoney;
    private boolean isBetting, isAllBetting;
    private Map map;
    private String rid;
    //TODO: 텍스트에 0입력시 방 삭제

    public Betting_Promise(@NonNull Context context, String r, String UID) {
        super(context);
        map = (Map)context; // context 캐스팅
        rid = r;
        isAllBetting = true;
        allBettingMoney = 0;
        setContentView(R.layout.activity_betting_promise);

        et_betting_coin = findViewById(R.id.et_betting);
        tv_betting_max = findViewById(R.id.tv_betting_coin);
        btn_betting = findViewById(R.id.id_betting_select);
        tv_curr_betting = findViewById(R.id.tv_curr_betting);
        usersUID = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //방에서 유저 UID를 가져옴
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Promise").child(rid).child("promisePlayer");
        bettingCoinListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HashMap<String, Object>> players = (List<HashMap<String, Object>>) snapshot.getValue();
                int money = 0;

                for(int z = 0; z < players.size(); z++){
                    String s = (String) players.get(z).get("playerUID");
                    usersUID.add(s);

                    //나를 식별
                    if(s.equals(UID)){
                        money = ((Long) players.get(z).get("bettingMoney")).intValue();
                        me_num = z;
                    }
                }
                //자신이 이미 배팅 했는지 판별
                if(money != 0){
                    isBetting = true;
                }
                else{
                    isBetting = false;
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
        startCurrVoteListener();

        btn_betting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_betting_coin.getText().toString().equals("") || tv_betting_max.getText().toString().equals("로딩중...")){
                    Toast.makeText(map, "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(currBettingNum != me_num){
                    Toast.makeText(map, "아직 자신의 배팅 순서가 아닙니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isBetting){
                    Toast.makeText(map, "이미 배팅 완료 하였습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(et_betting_coin.getText().toString().equals("0")){
                    /* 방 삭제 로직
                    0코인 배팅 시 show_alert_dial_0coin 로그를 띄움
                    확인 시 vote 값을 -1로 변경 후
                    show_alert_dial_removePromise를 띄워 모든 사용자에게 알림
                    확인 클릭 시 자신의 User객체에서 해당 방을 삭제함
                    ->방장이 끝까지 남아서 다른 사람이 다 나갈때 나갈수있음 -> 방삭제
                     */
                    show_alert_dial_0coin();
                    return;
                }
                else if(et_betting_coin.getText().toString().equals("1")){
                    mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(me_num)).child("bettingMoney").setValue(1);
                    isBetting = true;
                    Toast.makeText(map, "관전자 모드로 배팅 성공!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int val = Integer.parseInt(et_betting_coin.getText().toString());
                if(1 < val && val < 100){
                    Toast.makeText(map, "100코인 이상 배팅해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int bettingM = Integer.parseInt(et_betting_coin.getText().toString());
                mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(me_num)).child("bettingMoney").setValue(bettingM);
                isBetting = true;
                Toast.makeText(map, "배팅 성공!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //현재 배팅 상황을 실시간으로 중계
    public void startCurrVoteListener(){
        databaseReference = database.getReference("Promise").child(rid).child("promisePlayer");
        currVoteListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HashMap<String, Object>> players = (List<HashMap<String, Object>>) snapshot.getValue();
                int bMoney;
                //배팅을 아직 안한사람 닉네임 띄우기
                for(int i = 0; i < players.size(); i++) {
                    bMoney = ((Long) players.get(i).get("bettingMoney")).intValue();
                    if(bMoney == 0) {
                        tv_curr_betting.setText(players.get(i).get("nickName").toString() + "님이 배팅중 입니다.");
                        currBettingNum = i;
                        isAllBetting = false;
                        break;
                    }
                    else{
                        isAllBetting = true;
                    }
                }
                //배팅이 완료되면
                if(isAllBetting){
                    for(int i = 0; i < players.size(); i++){
                        //관전자 제외
                        if(((Long) players.get(i).get("bettingMoney")).intValue() != 1){
                            allBettingMoney += ((Long) players.get(i).get("bettingMoney")).intValue();
                        }
                    }
                    mDatabase.child("Promise").child(rid).child("bettingMoney").setValue(allBettingMoney);
                    map.voteComplete();
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(currVoteListener);
    }

    //TODO: 실시간 리스너에 달아주기
    public void show_alert_dial_removePromise(){
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(map);
        //alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("알림");
        myAlertBuilder.setMessage("불참여 인원이 있어 방이 삭제 되었습니다.");
        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){

            }
        });
        myAlertBuilder.show();
    }

    public void show_alert_dial_0coin(){
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(map);
        //alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("주의");
        myAlertBuilder.setMessage("정말로 0코인을 배팅 하시겠습니까?");
        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                mDatabase.child("Promise").child(rid).child("vote").setValue(-1);
            }
        });
        myAlertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        myAlertBuilder.show();
    }

    //바깥영역 터치방지
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return false;
    }

    //뒤로가기 시 다이얼로그와 map종료
    @Override
    public void onBackPressed() {
        dismiss();
        map.onBackPressed();
    }
}