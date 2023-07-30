package com.PACOsoft.promise_betting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class signup_page extends AppCompatActivity {
    private final String nick_validation = "^[a-z가-힇]+[a-z0-9가-힇]{1,10}$";
    private final String id_validation = "^[a-z]+[a-z0-9]{5,12}$";
    private final String pw_validation = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
    private TextInputEditText et_nick, et_id, et_pw, et_check;
    private TextInputLayout lo_nick, lo_id, lo_pw, lo_check;
    private TextView dupli_tv;
    private TextView signup_tv;
    private boolean[] booleans = {false, false, false, false, false}; // 0 : 닉네임, 1 : 아이디, 2 : 비밀번호, 3 : 비밀번호 확인, 4 : 중복확인
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        dupli_tv = findViewById(R.id.dupli_tv);
        signup_tv = findViewById(R.id.tv_signup);
        et_nick = findViewById(R.id.et_nickname);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_check = findViewById(R.id.et_pw_check);
        lo_nick = findViewById(R.id.lo_nickname);
        lo_id = findViewById(R.id.lo_id);
        lo_pw = findViewById(R.id.lo_pw);
        lo_check = findViewById(R.id.lo_pw_check);

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
                if(validation){
                    lo_nick.setError("");
                    booleans[0] = true;
                }
                else if(nickCheck.isEmpty()){
                    lo_nick.setError("닉네임을 입력해 주세요.");
                    booleans[0] = false;
                }
                else{
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
                booleans[4] = false;
                if(validation){
                    lo_id.setError("");
                    booleans[1] = true;
                    dupli_tv.setEnabled(true);
                }
                else if(idCheck.isEmpty()){
                    lo_id.setError("아이디를 입력해 주세요.");
                    booleans[1] = false;
                    dupli_tv.setEnabled(false);
                }
                else{
                    lo_id.setError(" * 영소문자, 숫자 조합으로 6자리 이상");
                    booleans[1] = false;
                    dupli_tv.setEnabled(false);
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
                if(validation){
                    lo_pw.setError("");
                    booleans[2] = true;
                }
                else if(pw.isEmpty()){
                    lo_pw.setError("비밀번호를 입력해 주세요.");
                    booleans[2] = false;
                }
                else{
                    lo_pw.setError(" * 영문자, 숫자, 특수문자(모두) 조합으로 8자리 이상");
                    booleans[2] = false;
                }
                if(!pwCheck.equals(pw)){
                    lo_check.setError(" * 비밀번호를 다시 입력해 주세요");
                    booleans[3] = false;
                }
                else{
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
                if(pw_Check.equals(pw)){
                    lo_check.setError("");
                    booleans[3] = true;
                }
                else if(pw_Check.isEmpty()){
                    lo_check.setError("비밀번호를 다시 입력해 주세요.");
                    booleans[3] = false;
                }
                else{
                    lo_check.setError(" * 비밀번호가 일치하지 않습니다.");
                    booleans[3] = false;
                }
                signup_enable();
            }
        });
    }

    public void btn_dupli_check(View view){
        String myId = et_id.getText().toString();
        Log.v("tt", myId);
        arrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트를 초기화
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getId().equals(myId))) {//사용자가 정한 id와 동일한 id가 DB에 있는지 확인
                    Toast toast = Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    booleans[4] = false;
                    signup_enable();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "사용 가능한 ID입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    booleans[4] = true;
                    signup_enable();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });
    }

    public void signup_enable(){
        if(booleans[0] && booleans[1] && booleans[2] && booleans[3] && booleans[4]){
            signup_tv.setEnabled(true);
        }
        else{
            signup_tv.setEnabled(false);
        }
    }

    public void btn_signup_close(View view) {
        finish();
    }

    public void btn_signup(View view){
        final String pw = et_pw.getText().toString();
        final String check = et_check.getText().toString();

        if(check.isEmpty()){
            lo_check.setError("비밀번호를 한 번 더 입력해 주세요.");
            booleans[3] = false;
        }
        else if(!check.equals(pw)){
            lo_check.setError("비밀번호가 일치하지 않습니다.");
            booleans[3] = false;
        }
        else{
            lo_check.setError("");
            booleans[3] = true;
        }
    }
}