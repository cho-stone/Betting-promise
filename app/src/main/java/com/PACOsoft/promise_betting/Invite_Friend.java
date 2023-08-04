package com.PACOsoft.promise_betting;

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
import android.widget.TextView;

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
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String myId;
    private HashSet<String> hashSet = new HashSet<>();//중복 방지 위해 해쉬셋 이용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int day = intent.getIntExtra("day", 0);
        int hour = intent.getIntExtra("hour", 0);
        int min = intent.getIntExtra("min", 0);
        int position = intent.getIntExtra("position", 0);
        myId = intent.getStringExtra("myId"); //Home에서 intent해준 id를 받아옴

        recyclerView = findViewById(R.id.inviteFriendsRecyclerview); // 아이디 연결
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
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getId().equals(myId))) {//myId와 동일한 id가 DB에 있는지 확인
                    Optional<User> anyElement = users.stream().parallel().filter(u -> u.getId().equals(myId)).findFirst();
                    //User에서 id가 myId와 동일한 객체를 필터링
                    String[] s = anyElement.get().getFriendsId().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다
                        for (String t : s) {
                            if (user.getId().equals(t)) {
                                arrayList.add(user);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
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

    public void btn_SearchFriend(View view) {//검색 버튼 누르면 실행

        TextView textView = (TextView) findViewById(R.id.et_search);//텍스트뷰 참조 객체 선언
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트를 초기화
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }

                if (users.stream().parallel().anyMatch(u -> u.getId().equals(myId))) {//myId와 동일한 id가 DB에 있는지 확인
                    Optional<User> anyElement = users.stream().parallel().filter(u -> u.getId().equals(myId)).findFirst();
                    //User에서 id가 myId와 동일한 객체를 필터링
                    String[] s = anyElement.get().getFriendsId().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다
                        for (String t : s) {
                            if (textView.getText().toString().equals("")) {
                                if (user.getId().equals(t))
                                    arrayList.add(user);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                            } else if (user.getId().equals(t) && user.getId().equals(textView.getText().toString()))
                                arrayList.add(user);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                        }
                    }
                    adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
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

    public void btn_UserClicked(View v) {
        ColorDrawable color = (ColorDrawable) v.getBackground();
        int bgcolor = color.getColor();
        TextView textView = v.findViewById(R.id.tv_id);
        if (bgcolor == Color.LTGRAY) {
            v.setBackgroundColor(Color.WHITE);
            hashSet.remove(String.valueOf(textView.getText()));
        } else {
            v.setBackgroundColor(Color.LTGRAY);
            hashSet.add(String.valueOf(textView.getText()));
        }
    }

    public void btv_Invite_Friend_Clicked(View v) {

        System.out.println(hashSet);

        //todo: intent받아온 값과 여기서 선택한 친구id를 전부 방을 생성하며 DB에 기록
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }
}


