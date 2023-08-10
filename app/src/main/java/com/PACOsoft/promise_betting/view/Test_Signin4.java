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
            auth.createUserWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "계정 생성을 성공했습니다", Toast.LENGTH_LONG).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            auth.signInWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                                    .addOnCompleteListener(
                                            result -> {
                                                if (result.isSuccessful()) {
                                                    User user = new User();
                                                    user.setProfile("https://firebasestorage.googleapis.com/v0/b/fir-listexample-4b146.appspot.com/o/free-icon-font-user-3917688.png?alt=media&token=6d701d27-9620-4b12-b315-46fa39a42210");
                                                    user.setAccount(0);
                                                    user.setId(ID.getText().toString().trim());
                                                    user.setNickName(Nick.getText().toString().trim());
                                                    user.setPw(Password.getText().toString().trim());
                                                    user.setPromiseKey("");
                                                    user.setFriendsId("");
                                                    String UID = auth.getCurrentUser().getUid();
                                                    databaseReference.child("User").child(UID).setValue(user);
                                                } else {
                                                }
                                            }
                                    );
                            auth.signOut();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "이미 존재하는 계정입니다", Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
