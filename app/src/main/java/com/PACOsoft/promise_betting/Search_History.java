package com.PACOsoft.promise_betting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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
    private ArrayList<History> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    private String myId = "1213";

    private String[] s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        recyclerView = findViewById(R.id.historyRecyclerView); // 아이디 연결
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
                    s = anyElement.get().getPromiseKey().split(" ");//위에서 필터링한 객체의 PromiseKey를 공백을 기준으로 스플릿 해서 배열에 저장

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });



        databaseReference2 = database.getReference("History");//DB테이블 연결, 파이어베이스 콘솔에서 History에 접근
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트를 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    History history = snapshot.getValue(History.class); // 만들어뒀던 User 객체에 데이터를 담는다
                    for(String temp : s) {
                        Log.e("temp", temp);
                        if(history.getPromiseKey().equals(temp)) {
                            arrayList.add(history);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                        }
                    }

                }
                adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });

        adapter = new History_List_Adapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

    }

}