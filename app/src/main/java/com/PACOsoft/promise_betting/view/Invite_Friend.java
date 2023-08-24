package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.PACOsoft.promise_betting.Adapter.User_List_Adapter;
import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class Invite_Friend extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2;
    private String UID, TAG;
    private String[] friends;
    private ValueEventListener getInviteFriendListValueEventLister, getInviteFriendListValueEventLister2, getInviteFriendListValueEventLister3, getInviteFriendListValueEventLister4;
    private HashSet<String> hashSet = new HashSet<>();//중복 방지 위해 해쉬셋 이용, UID 저장용
    private HashSet<String> hashSet2 = new HashSet<>();//중복 방지 위해 해쉬셋 이용, nickname 저장용
    private HashSet<String> hashSet3 = new HashSet<>();//중복 방지 위해 해쉬셋 이용, id 저장용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        Intent intent = getIntent();
        TAG = "Invite_Friend";
        UID = intent.getStringExtra("UID"); //Home에서 intent해준 UID를 받아옴
        view_my_all_friend();
    }

    public void btn_SearchFriend(View view) {//검색 버튼 누르면 실행
        databaseReference.removeEventListener(getInviteFriendListValueEventLister);
        TextView textView = (TextView) findViewById(R.id.et_search);//텍스트뷰 참조 객체 선언
        ArrayList<User> arrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        getInviteFriendListValueEventLister4 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User friend = snapshot.getValue(User.class);
                if (friend.getId().equals(textView.getText().toString())) {
                    arrayList.add(friend);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        getInviteFriendListValueEventLister3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear(); //기존 배열리스트를 초기화
                User me = snapshot.getValue(User.class);
                friends = me.getFriendsUID().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                for (String friend : friends) {
                    databaseReference2 = database.getReference("User").child(friend);
                    databaseReference2.addListenerForSingleValueEvent(getInviteFriendListValueEventLister4);
                    databaseReference2.removeEventListener(getInviteFriendListValueEventLister4);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addValueEventListener(getInviteFriendListValueEventLister3);
        adapter = new User_List_Adapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }

    public void btn_UserClicked(@NonNull View v) {
        ColorDrawable color = (ColorDrawable) v.getBackground();
        int bgcolor = color.getColor();
        TextView tv_UID = v.findViewById(R.id.tv_UID);
        TextView tv_nickName = v.findViewById(R.id.tv_nickName);
        TextView tv_id = v.findViewById(R.id.tv_id);
        if (bgcolor == Color.LTGRAY) {
            v.setBackgroundColor(Color.WHITE);
            hashSet.remove(String.valueOf(tv_UID.getText()));
            hashSet2.remove(String.valueOf(tv_nickName.getText()));
            hashSet3.remove(String.valueOf(tv_id.getText()));
        } else {
            v.setBackgroundColor(Color.LTGRAY);
            hashSet.add(String.valueOf(tv_UID.getText()));
            hashSet2.add(String.valueOf(tv_nickName.getText()));
            hashSet3.add(String.valueOf(tv_id.getText()));
        }
    }

    public void btv_Invite_Friend_Clicked(View v) {
        ArrayList<String> friend_arr = new ArrayList<>(hashSet);
        ArrayList<String> friend_arr2 = new ArrayList<>(hashSet2);
        if (friend_arr.size() == 0) {
            Toast.makeText(getApplicationContext(), "1명 이상 선택", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("friends", friend_arr);
        resultIntent.putExtra("friends2", friend_arr2);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void view_my_all_friend() {
        recyclerView = findViewById(R.id.inviteFriendsRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<User> userArrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        getInviteFriendListValueEventLister2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.add(snapshot.getValue(User.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        getInviteFriendListValueEventLister = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear(); //기존 배열리스트를 초기화
                User me = snapshot.getValue(User.class);
                String[] friends = me.getFriendsUID().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                for (String friend : friends) {
                    databaseReference2 = database.getReference("User").child(friend);
                    databaseReference2.addListenerForSingleValueEvent(getInviteFriendListValueEventLister2);
                    databaseReference2.removeEventListener(getInviteFriendListValueEventLister2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addValueEventListener(getInviteFriendListValueEventLister);
        adapter = new User_List_Adapter(userArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }
}


