package com.PACOsoft.promise_betting.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Test_Signin4 extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText Nick;
    private TextInputEditText ID;
    private TextView Password;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        auth = FirebaseAuth.getInstance();
    }

    public void Test4_btn_signup(View view) {
        Nick = (TextInputEditText) findViewById(R.id.test4_et_Nick);
        ID = (TextInputEditText) findViewById(R.id.test4_et_ID);
        Password = (TextView) findViewById(R.id.test4_et_PWCheck);
        signup();
    }

    public void Test4_btn_signup_close(View view) {
        finish();
    }

    public void signup() {
        try {
            //회원가입 시작
            auth.createUserWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "인증 메일을 확인 해주세요", Toast.LENGTH_LONG).show();
                            //사용자 인증 메일 보내기
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(verifiTask -> {
                                        if (verifiTask.isSuccessful()) {//이메일 인증 성공
                                            databaseReference = FirebaseDatabase.getInstance().getReference();//DB연결
                                            //사용자 정보 유저 객체에 담아서 DB에 저장
                                            User user = new User();
                                            user.setProfile("https://firebasestorage.googleapis.com/v0/b/fir-listexample-4b146.appspot.com/o/free-icon-font-user-3917688.png?alt=media&token=6d701d27-9620-4b12-b315-46fa39a42210");
                                            user.setAccount(0);
                                            user.setId(ID.getText().toString().trim());
                                            user.setNickName(Nick.getText().toString().trim());
                                            user.setPromiseKey("");
                                            user.setFriendsId("");
                                            user.setUID(auth.getCurrentUser().getUid());
                                            databaseReference.child("User").child(auth.getCurrentUser().getUid()).setValue(user);
                                            //finish();
                                            Intent intent = new Intent(getApplicationContext(), Test_Signin3.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//기존 모든 엑티비티 종료 후 intent
                                            startActivity(intent);
                                        } else {//이메일 인증 실패
                                            Toast.makeText(getApplicationContext(), "이메일 인증 실패", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(getApplicationContext(), "올바르지 않은 형식입니다", Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
