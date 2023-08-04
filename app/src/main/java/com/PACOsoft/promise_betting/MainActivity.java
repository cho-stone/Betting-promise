package com.PACOsoft.promise_betting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText ID;
    private TextInputEditText Password;
    private String loginId, loginPw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE); //자동로그인 키값 생성
        loginId = auto.getString("inputId", null); //처음에는 null값 삽입
        loginPw = auto.getString("inputPw", null);

        if(loginPw != null && loginId != null){
            Intent intent = new Intent(getApplicationContext(), Home.class);//Home으로 intent
            intent.putExtra("myId", loginId);//ID 정보 intent
            intent.putExtra("myPassword", loginPw);//Password 정보 intent
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//기존 모든 엑티비티 종료 후 intent
            startActivity(intent);
        }
    }

    public void login(View view) {
        ID = findViewById(R.id.inputLoginId);
        Password = findViewById(R.id.inputLoginPassword);

        ArrayList arrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        FirebaseDatabase database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        DatabaseReference databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트를 초기화
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getId().equals(ID.getText().toString()))) {//사용자가 정한 id와 동일한 id가 DB에 있는지 확인
                    {
                        Optional<User> anyElement = users.stream().parallel().filter(u -> u.getId().equals(ID.getText().toString())).findFirst();//ID가 동일한 객체를 anyElement에 담음
                        if (anyElement.get().getPw().equals(Password.getText().toString())) {//비밀번호까지 DB와 일치하면 로그인 성공
                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE); //자동로그인 키값 생성
                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("inputId", ID.getText().toString());//아이디와 비밀번호 값 삽입
                            autoLogin.putString("inputPw", Password.getText().toString());
                            autoLogin.commit(); //데이터 저장
                            Intent intent = new Intent(getApplicationContext(), Home.class);//Home으로 intent
                            intent.putExtra("myId", ID.getText().toString());//ID 정보 intent
                            intent.putExtra("myPassword", Password.getText().toString());//Password 정보 intent
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//기존 모든 엑티비티 종료 후 intent
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "ID가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, signup_page.class);
        startActivity(intent);
    }

}