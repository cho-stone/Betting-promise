package com.PACOsoft.promise_betting.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.PACOsoft.promise_betting.R;
import com.google.firebase.auth.FirebaseAuth;


public class Option extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        mAuth = FirebaseAuth.getInstance();

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void btnSignOut(View view) {
        signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btnAppClose(View view) {
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
        FirebaseAuth.getInstance().signOut();
    }

    public void btn_revoke(View view) {
        revokeAccess();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }






}
