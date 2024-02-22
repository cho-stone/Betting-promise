package com.PACOsoft.promise_betting.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class Select_Profile extends Activity {
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
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_select_profile);
            Intent intent = getIntent();
            UID = intent.getStringExtra("UID");
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("User").child(UID).child("profile");
        }

        public void btnChangeProfile1(View view) {//프로필 이미지 변경
            databaseReference.setValue("https://firebasestorage.googleapis.com/v0/b/promise-betting.appspot.com/o/userIcon.png?alt=media&token=0da7a58e-d6fc-4d6d-9ece-383ba8afc9df");
        }
        public void btnChangeProfile2(View view) {//프로필 이미지 변경
            databaseReference.setValue("https://firebasestorage.googleapis.com/v0/b/promise-betting.appspot.com/o/userIcon2.png?alt=media&token=d7ff09a8-6182-4ae1-a733-2ba90ab4b7d0");
        }
        public void btnChangeProfile3(View view) {//프로필 이미지 변경
            databaseReference.setValue("https://firebasestorage.googleapis.com/v0/b/promise-betting.appspot.com/o/userIcon3.png?alt=media&token=86a9daf8-c5dd-41f2-bb8d-ecfd3df6e778");
        }
        public void btnChangeProfile4(View view) {//프로필 이미지 변경
            databaseReference.setValue("https://firebasestorage.googleapis.com/v0/b/promise-betting.appspot.com/o/ufo.png?alt=media&token=aaaec409-def7-4699-b44b-609e6d952759");
        }
        public void btnChangeProfile5(View view) {//프로필 이미지 변경
            databaseReference.setValue("https://firebasestorage.googleapis.com/v0/b/promise-betting.appspot.com/o/alien.png?alt=media&token=89c68b68-94a5-4b6a-a67e-9f8e0e3a4393");
        }
        public void btnChangeProfile6(View view) {//프로필 이미지 변경
            databaseReference.setValue("https://firebasestorage.googleapis.com/v0/b/promise-betting.appspot.com/o/question.png?alt=media&token=cb79e14f-f863-4724-92ca-e904ae1528fb");
        }
}
