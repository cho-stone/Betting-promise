package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.PACOsoft.promise_betting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Option extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private String UID;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User").child(UID);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void btnSignOut(View view) {//로그아웃
        Intent intent = new Intent(this, MainActivity.class);
        databaseReference.removeEventListener(((Home)Home.context).getFriendValueEventLister);
        databaseReference.removeEventListener(((Home)Home.context).getPromiseValueEventListener);
        signOut();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//실행 중인 모든 엑티비티 종료
        startActivity(intent);
    }

    public void btnAppClose(View view) {//앱 종료
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

    private void revokeAccess() {//회원탈퇴
        mAuth.getCurrentUser().delete();
        FirebaseAuth.getInstance().signOut();
    }

    private void removeDB(){
        database = FirebaseDatabase.getInstance();
        database.getReference("User").child(UID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v("removeDB", "성공");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error: "+e.getMessage());
            }
        });
    }

    public void btn_revoke(View view) {
        revokeAccess();
        removeDB();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
