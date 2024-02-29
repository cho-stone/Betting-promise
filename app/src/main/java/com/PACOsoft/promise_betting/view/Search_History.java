package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.PACOsoft.promise_betting.Adapter.History_List_Adapter;
import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.History;
import com.PACOsoft.promise_betting.obj.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

public class Search_History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2;
    private String UID;
    private String[] historys;
    private ValueEventListener getHistoryValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID"); //Home에서 intent해준 UID를 받아옴
        recyclerView = findViewById(R.id.historyRecyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<History> historyArrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        getHistoryValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                historyArrayList.clear(); //기존 배열리스트를 초기화
                User me = snapshot.getValue(User.class);
                if(me.getHistoryKey().equals("")){return;}
                historys = me.getHistoryKey().split(" ");//위에서 필터링한 객체의 PromiseKey를 공백을 기준으로 스플릿 해서 배열에 저장
                for(String history : historys){
                    databaseReference2 =database.getReference("History").child(history);//DB테이블 연결, 파이어베이스 콘솔에서 History에 접근
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            historyArrayList.add(snapshot.getValue(History.class));
                            adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        databaseReference.addValueEventListener(getHistoryValueEventListener);
        adapter = new History_List_Adapter(historyArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference.removeEventListener(getHistoryValueEventListener);
        finish();
    }
}