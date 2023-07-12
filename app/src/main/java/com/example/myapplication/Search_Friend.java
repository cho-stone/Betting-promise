package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatabaseReference databaseReference;
    private User_List_Adapter adapter;

    private Dialog Add_Friend;

    private Optional<User> anyElement;
    private Optional<User> anyElement2;

    private String temp;
    private String myId = "1213";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
    }

    public void btn_SearchFriend(View view) {//검색 버튼 누르면 실행
        recyclerView = findViewById(R.id.friendsRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트를 초기화
                temp = "";//임시 스트링 초기화
                ArrayList<User> users = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                TextView textView = (TextView) findViewById(R.id.et_search);//텍스트뷰 참조 객체 선언

                if (users.stream().parallel().anyMatch(u -> u.getId().equals(textView.getText().toString()))) {//텍스트뷰에서 가져온 텍스트와 동일한 id가 유에 있는지 확인
                    anyElement = users.stream().parallel().filter(u -> u.getId().equals(textView.getText().toString())).findFirst();
                    anyElement2 = users.stream().parallel().filter(u -> u.getId().equals(myId)).findFirst();
                    //DB에 동일한 ID가 존재한다면 텍스트뷰 참조 객체에서 입력된 텍스트 받아와서 DB의 id와 동일한 객체 찾음
                    String s = anyElement.get().getId();
                    String s2 = anyElement2.get().getFriendsId();
                    temp = s2 + " " + s;
                    //찾은 객체의 id가져와서 스트링에 저장
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다
                        if (user.getId().equals(s))
                            arrayList.add(user);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                    }
                    adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "일치하는 ID가 없습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });

        adapter = new User_List_Adapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }

    public void btn_UserClicked(View view) {
        //arrayList를 다이얼로그로 넘겨줌
        Add_Friend = new Add_Friend(this, arrayList);
        Add_Friend.setCancelable(false);//다이얼로그 띄우는 동안 뒷배경화면 클릭 방지
        Add_Friend.show();
    }

    public void btn_add_friend(View view) {//친구 추가 버튼
        databaseReference.child(myId).child("friendsId").setValue(temp);
        Add_Friend.cancel();
    }

    public void btn_add_friend_cancel(View view) {//취소 버튼
        Add_Friend.cancel();
    }

}