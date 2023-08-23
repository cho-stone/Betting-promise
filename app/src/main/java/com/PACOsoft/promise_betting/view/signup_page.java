package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class signup_page extends AppCompatActivity {
    private final String nick_validation = "^[a-z가-힇]+[a-z0-9가-힇]{1,10}$";
    private final String id_validation = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    private final String pw_validation = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$";
    private TextInputEditText et_nick, et_id, et_pw, et_check;
    private TextInputLayout lo_nick, lo_id, lo_pw, lo_check;
    private TextView signup_tv;
    private boolean[] booleans = {false, false, false, false}; // 0 : 닉네임, 1 : 아이디, 2 : 비밀번호, 3 : 비밀번호 확인
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        signup_tv = findViewById(R.id.tv_signup);
        et_nick = findViewById(R.id.et_nickname);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_check = findViewById(R.id.et_pw_check);
        lo_nick = findViewById(R.id.lo_nickname);
        lo_id = findViewById(R.id.lo_id);
        lo_pw = findViewById(R.id.lo_pw);
        lo_check = findViewById(R.id.lo_pw_check);
        auth = FirebaseAuth.getInstance();

        et_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String nickCheck = et_nick.getText().toString();
                boolean validation = Pattern.matches(nick_validation, nickCheck);
                if (validation) {
                    lo_nick.setError("");
                    booleans[0] = true;
                } else if (nickCheck.isEmpty()) {
                    lo_nick.setError("닉네임을 입력해 주세요.");
                    booleans[0] = false;
                } else {
                    lo_nick.setError("닉네임 형식이 올바르지 않습니다.");
                    booleans[0] = false;
                }
                signup_enable();
            }
        });

        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String idCheck = et_id.getText().toString();
                boolean validation = Pattern.matches(id_validation, idCheck);
                if (validation) {
                    lo_id.setError("");
                    booleans[1] = true;
                } else if (idCheck.isEmpty()) {
                    lo_id.setError("아이디를 입력해 주세요.");
                    booleans[1] = false;
                } else {
                    lo_id.setError("* 영소문자, 숫자 조합으로 6자리 이상");
                    booleans[1] = false;
                }
                signup_enable();
            }
        });

        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pw = et_pw.getText().toString();
                String pwCheck = et_check.getText().toString();
                boolean validation = Pattern.matches(pw_validation, pw);
                if (validation) {
                    lo_pw.setError("");
                    booleans[2] = true;
                } else if (pw.isEmpty()) {
                    lo_pw.setError("비밀번호를 입력해 주세요.");
                    booleans[2] = false;
                } else {
                    lo_pw.setError("* 영문자, 숫자, 특수문자(모두) 조합으로 8자리 이상");
                    booleans[2] = false;
                }
                if (!pwCheck.equals(pw)) {
                    lo_check.setError("* 비밀번호를 다시 입력해 주세요");
                    booleans[3] = false;
                } else {
                    lo_check.setError("");
                    booleans[3] = true;
                }
                signup_enable();
            }
        });

        et_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pw = et_pw.getText().toString();
                String pw_Check = et_check.getText().toString();
                if (pw_Check.equals(pw)) {
                    lo_check.setError("");
                    booleans[3] = true;
                } else if (pw_Check.isEmpty()) {
                    lo_check.setError("비밀번호를 다시 입력해 주세요.");
                    booleans[3] = false;
                } else {
                    lo_check.setError("* 비밀번호가 일치하지 않습니다.");
                    booleans[3] = false;
                }
                signup_enable();
            }
        });
    }

    public void signup_enable() {
        if (booleans[0] && booleans[1] && booleans[2] && booleans[3]) {
            signup_tv.setEnabled(true);
        } else {
            signup_tv.setEnabled(false);
        }
    }

    public void btn_signup_close(View view) {
        finish();
    }

    public void btn_signup(View view) {
        try {
            //회원가입 시작
            auth.createUserWithEmailAndPassword(et_id.getText().toString().trim(), et_pw.getText().toString().trim())
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
                                            user.setId(et_id.getText().toString().trim());
                                            user.setNickName(et_nick.getText().toString().trim());
                                            user.setPromiseKey("");
                                            user.setFriendsUID("");
                                            user.setHistoryKey("");
                                            user.setUID(auth.getCurrentUser().getUid());
                                            databaseReference.child("User").child(auth.getCurrentUser().getUid()).setValue(user);
                                            finish();
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
        finish();
    }
}