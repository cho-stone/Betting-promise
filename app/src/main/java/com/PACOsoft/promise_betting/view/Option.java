package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.User;
import com.PACOsoft.promise_betting.view.MainActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.PACOsoft.promise_betting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Option extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private String UID;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Intent deleteIntent;
    public static ValueEventListener getMyInfoListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User").child(UID);
        getMyInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User me = snapshot.getValue(User.class);
                TextView id = (TextView) findViewById(R.id.op_tv_id);
                id.setText(me.getId());
                TextView nickname = (TextView) findViewById(R.id.op_tv_nickName);
                nickname.setText(me.getNickName());
                ImageView profile = (ImageView) findViewById(R.id.iv_op_profile);
                Glide.with(profile)
                        .load(me.getProfile()).into(profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        //databaseReference.addListenerForSingleValueEvent(getMyInfoListener);
        databaseReference.addValueEventListener(getMyInfoListener);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
    public void btnChangeProfile(View view) {//프로필 이미지 변경으로 이동
            Intent intent = new Intent(this, Select_Profile.class);
            intent.putExtra("UID", UID);//ID 정보 intent
            startActivity(intent);
    }


    public void btnSignOut(View view) {//로그아웃
        Intent intent = new Intent(this, MainActivity.class);
        boolean isFriendView = ((Home)Home.context).isFriendView;
        if(isFriendView) databaseReference.removeEventListener(((Home)Home.context).getFriendListValueEventLister);
        else databaseReference.removeEventListener(((Home)Home.context).getPromiseListValueEventListener);
        signOut();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//실행 중인 모든 엑티비티 종료
        startActivity(intent);
    }

    public void btnAppClose(View view) {//앱 종료
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

//    private void revokeAccess() {//회원탈퇴
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("success", "User account deleted.");
//                            deleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(deleteIntent);
//                        }
//                        else
//                        {
//                            Log.d("success", "FAIL");
//                        }
//                    }
//                });
//    }

//    private void removeDB(){
//        //database = FirebaseDatabase.getInstance();
//        database.getReference("User").child(UID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.v("removeDB", "성공");
//                System.out.println("error: 성공");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("error: "+e.getMessage());
//                System.out.println("error: 실패");
//            }
//        });
//    }

    private void removeDB(){//내 정보 삭제
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User").child(UID);
        database.getReference("User").child(UID).removeValue();
    }

    private void revokeAccess()//회원탈퇴
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("option", "User account deleted.");
                            deleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//실행 중인 모든 엑티비티 종료
                            startActivity(deleteIntent);
                        }
                    }
                });
    }

    public void btn_revoke(View view) {
        deleteIntent = new Intent(this, MainActivity.class);
        boolean isFriendView = ((Home)Home.context).isFriendView;//내가 Home에서 어떤 목록을 보고있었는지 체크
        if(isFriendView) databaseReference.removeEventListener(((Home)Home.context).getFriendListValueEventLister);//친구목록 보고있었다면 친구 리스너 끔
        else databaseReference.removeEventListener(((Home)Home.context).getPromiseListValueEventListener);//약속목록 보고있었다면 약속 리스너 끔
        removeDB();
        revokeAccess();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference.removeEventListener(getMyInfoListener);
        finish();
    }
}
