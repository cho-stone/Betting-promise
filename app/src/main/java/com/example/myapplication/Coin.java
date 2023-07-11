package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

public class Coin extends Activity {
    private int tempcoin;
    private String myId = "1213";
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
        TextInputEditText et_coin = findViewById(R.id.et_coin);
        tempcoin = Integer.valueOf(String.valueOf(et_coin.getText()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        DatabaseReference databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DataSnapshot) {
                ArrayList<User> users = new ArrayList<>();

                for (DataSnapshot snapshot : DataSnapshot.getChildren()) {//데이터 베이스 내의 User객체들은 전부 User타입의 배열리스트 users에 추가
                    users.add(snapshot.getValue(User.class));
                }

                if (users.stream().parallel().anyMatch(u -> u.getId().equals(myId))) {//그 중 myId와 같은 id 있는지 탐색
                    //myId와 같은 id가 있다면 그게 내 객체이므로 그 객체를 anyElement에 저장
                    Optional<User> anyElement = users.stream().parallel().filter(u -> u.getId().equals(myId)).findFirst();
                    //나의 account 불러와서 내가 충전에 입력한 coin과 더해줌
                    int tempcoin2 = anyElement.get().getAccount();
                    tempcoin = tempcoin + tempcoin2;
                }
                    databaseReference.child(myId).child("account").setValue(tempcoin);//더해준 최종 값 DB에 추가
                     TextView text = (TextView) findViewById(R.id.tv_afterCoin);//코인 충전 후 TextView 참조 객체 선언
                    text.setText(String.valueOf(tempcoin));//위에서 선언한 참조 객체에 값 넘겨줌
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        finish();
    }
}