package com.PACOsoft.promise_betting.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Test_Signin4 extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText ID;
    private TextView Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        auth = FirebaseAuth.getInstance();


    }


    public void btn_signup(View view) {
        ID = (TextInputEditText)findViewById(R.id.et_id);
        Password = (TextView)findViewById(R.id.lo_pw_check);
        try {
            auth.createUserWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "계정 생성 성공", Toast.LENGTH_LONG).show();
                            Log.e("T","성공");
                            finish();
                        }
                        else{
                            Log.e("T","이미 존재하는 계정");
                            Toast.makeText(getApplicationContext(), "이미 존재하는 계정", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void btn_signup_close(View view){
        finish();
    }


}
