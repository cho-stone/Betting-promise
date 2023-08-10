package com.PACOsoft.promise_betting.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;
import com.google.firebase.auth.FirebaseAuth;

public class Test_Signin2 extends AppCompatActivity implements View.OnClickListener {
    Button btnRevoke, btnLogout;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        btnLogout = (Button)findViewById(R.id.btn_signout);
        btnRevoke = (Button)findViewById(R.id.btn_revoke);
        mAuth = FirebaseAuth.getInstance();
        btnLogout.setOnClickListener(this);
        btnRevoke.setOnClickListener(this);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_signout) {
            signOut();
            finishAffinity();
        } else if (v.getId() == R.id.btn_revoke) {
            revokeAccess();
            finishAffinity();
        }

    }
}