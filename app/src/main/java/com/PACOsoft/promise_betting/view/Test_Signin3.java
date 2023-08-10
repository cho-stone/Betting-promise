package com.PACOsoft.promise_betting.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Test_Signin3 extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText ID;
    private TextInputEditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        auth = FirebaseAuth.getInstance();


    }


    public void login(View view) {
        ID = findViewById(R.id.inputLoginId);
        Password = findViewById(R.id.inputLoginPassword);
        signIn();
    }

    public void reg(View view) {
        Intent intent = new Intent(this, Test_Signin4.class);
        startActivity(intent);
    }


    public void signIn() {
        if (ID.getText().toString().trim().isEmpty() || ID.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "ID err", Toast.LENGTH_LONG).show();
        } else if (Password.getText().toString().trim().isEmpty() || Password.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "Password err", Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), Test_Signin2.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                                }
                            }

                    );
        }
    }
}





