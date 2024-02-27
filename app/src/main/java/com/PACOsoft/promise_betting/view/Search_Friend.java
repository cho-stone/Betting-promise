package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Optional;

public class Search_Friend extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2;
    private User_List_Adapter adapter;
    private Dialog Add_Friend;
    private String[] FriendsUID;
    private String UID, TAG, temp, FriendUID;
    private ValueEventListener getFriendValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID"); //Home에서 intent해준 UID를 받아옴
        TAG = "Search_Friend";
        Add_Friend = null;
    }

    public void btn_SearchFriend(View view) {//검색 버튼 누르면 실행
        recyclerView = findViewById(R.id.friendsRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        TextView textView = (TextView) findViewById(R.id.et_search);//텍스트뷰 참조 객체 선언
        ValueEventListener getFriendValueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    User friend = user.getValue(User.class);
                    if (!textView.getText().toString().equals(friend.getId())) {
                        continue;
                    } else if (textView.getText().toString().equals(friend.getId())) {
                        boolean isMyFriend = false;
                        for (String friendUID : FriendsUID) {
                            if (friendUID.equals(friend.getUID())) {
                                Toast toast = Toast.makeText(getApplicationContext(), "이미 존재하는 친구입니다.", Toast.LENGTH_SHORT);
                                toast.show();
                                isMyFriend = true;
                                break;
                            }
                        }
                        if (!isMyFriend) {
                            arrayList.add(friend);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                            adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                            String s1 = friend.getUID(); //친구의 UID
                            if (temp.equals("")) {
                                temp = s1;
                            } else {
                                temp += " " + s1;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        getFriendValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear(); //기존 배열리스트를 초기화
                        User me = snapshot.getValue(User.class);
                        if (textView.getText().toString().equals(me.getId())) {
                            Toast toast = Toast.makeText(getApplicationContext(), "자기 자신은 추가할 수 없습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        FriendsUID = me.getFriendsUID().split(" ");
                        temp = me.getFriendsUID().toString();
                        databaseReference2 = database.getReference("User");
                        databaseReference2.addListenerForSingleValueEvent(getFriendValueEventListener2);
                        databaseReference2.removeEventListener(getFriendValueEventListener2);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
        databaseReference = database.getReference("User").child(UID);
        databaseReference.addListenerForSingleValueEvent(getFriendValueEventListener);
        adapter = new User_List_Adapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        databaseReference.removeEventListener(getFriendValueEventListener);
    }

    public void btn_UserClicked(View view) {
        //arrayList를 다이얼로그로 넘겨줌
        if(Add_Friend == null) {
            Add_Friend = new Add_Friend(this, arrayList);
            Add_Friend.setCancelable(false);//다이얼로그 띄우는 동안 뒷배경화면 클릭 방지
            Add_Friend.show();
        }
    }

    public void btn_add_friend(View view) {//친구 추가 버튼
        databaseReference.child("friendsUID").setValue(temp);
        Add_Friend.cancel();
        Toast toast = Toast.makeText(getApplicationContext(), "친구 추가 완료", Toast.LENGTH_SHORT);
        toast.show();
        Add_Friend = null;
    }

    public void btn_add_friend_cancel(View view) {//취소 버튼
        Add_Friend.cancel();
        Add_Friend = null;
    }
}