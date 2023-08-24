package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.widget.TextView;

import com.PACOsoft.promise_betting.Adapter.History_List_Adapter;
import com.PACOsoft.promise_betting.Adapter.Promise_List_Adapter;
import com.PACOsoft.promise_betting.Adapter.User_List_Adapter;
import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.History;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Promise> promiseArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2;
    private String TAG, UID;
    private int coin;
    public static Context context;
    public static ValueEventListener getFriendListValueEventLister, getPromiseListValueEventListener;
    public boolean isFriendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        isFriendView = true;
        context = this;
        TAG = "Home";
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID"); //mainActivity에서 intent해준 id를 받아옴
        view_friends();
    }

    //Home에서 SearchFriend로 이동하는 버튼 구현
    public void btnSearchFriendClicked(View view) {
        Intent intent = new Intent(this, Search_Friend.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        startActivity(intent);
    }

    //Home에서 SearchHistory로 이동하는 버튼 구현
    public void btnSearchHistoryClicked(View view) {
        Intent intent = new Intent(this, Search_History.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        startActivity(intent);
    }

    //홈에서 유저 눌렀을 때 안팅기게 만듦
    public void btn_UserClicked(View view) {
    }

    //Home에서 Coin으로 이동하는 버튼 구현
    public void btnCoinsClicked(View view) {
        Intent intent = new Intent(this, Coin.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        startActivity(intent);
    }

    //Home에서 Create_Room으로 이동하는 버튼 구현
    public void btnCreateRoomClicked(View view) {
        Intent intent = new Intent(this, Create_Room.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        startActivity(intent);
    }

    //Home에서 Option으로 이동하는 버튼 구현
    public void btnOptionClicked(View view) {
        Intent intent = new Intent(this, Option.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        startActivity(intent);
    }

    public void btn_home_friend(View view) {
        view_friends();
    }

    public void btn_home_promise(View view) {
        view_promise();
    }

    public void btn_promiseClicked(@NonNull View v) {
        TextView tv_promiseKey = v.findViewById(R.id.tv_promiseKey);
        Intent intent = new Intent(this, Map.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        intent.putExtra("rid", tv_promiseKey.getText().toString());
        startActivity(intent);
    }

    public void view_friends() {
        recyclerView = findViewById(R.id.homeRecyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<User> userArrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        if(isFriendView == false) databaseReference.removeEventListener(getPromiseListValueEventListener);
        isFriendView = true;
            ValueEventListener getFriendListValueEventLister2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.add(snapshot.getValue(User.class));
                if(!snapshot.getValue(User.class).getId().equals(""))
                    adapter.notifyDataSetChanged();
                else{
                    userArrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        getFriendListValueEventLister = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear(); //기존 배열리스트를 초기화
                User me = snapshot.getValue(User.class);
                String[] friends = me.getFriendsUID().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                coin = me.getAccount();//내 객체에서 account값 가져옴
                TextView text = (TextView) findViewById(R.id.tv_point);//TextView 참조 객체 선언
                text.setText(String.valueOf(coin));//위에서 선언한 참조 객체에 값 넘겨줌
                for (String friend : friends) {
                    databaseReference2 = database.getReference("User").child(friend);
                    databaseReference2.addListenerForSingleValueEvent(getFriendListValueEventLister2);
                    databaseReference2.removeEventListener(getFriendListValueEventLister2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addValueEventListener(getFriendListValueEventLister);
        adapter = new User_List_Adapter(userArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }

    public void view_promise() {
        recyclerView = findViewById(R.id.homeRecyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Promise> promiseArrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        if(isFriendView) databaseReference.removeEventListener(getFriendListValueEventLister);
        isFriendView = false;
        ValueEventListener getPromiseListValueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promiseArrayList.add(snapshot.getValue(Promise.class));
                if(!snapshot.getValue(Promise.class).getPromiseKey().equals(""))
                    adapter.notifyDataSetChanged();
                else{
                    promiseArrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        getPromiseListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promiseArrayList.clear(); //기존 배열리스트를 초기화
                User me = snapshot.getValue(User.class);
                String[] promises = me.getPromiseKey().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                coin = me.getAccount();//내 객체에서 account값 가져옴
                TextView text = (TextView) findViewById(R.id.tv_point);//TextView 참조 객체 선언
                text.setText(String.valueOf(coin));//위에서 선언한 참조 객체에 값 넘겨줌
                for (String promise : promises) {
                    databaseReference2 = database.getReference("Promise").child(promise);
                    databaseReference2.addListenerForSingleValueEvent(getPromiseListValueEventListener2);
                    databaseReference2.removeEventListener(getPromiseListValueEventListener2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addValueEventListener(getPromiseListValueEventListener);
        adapter = new Promise_List_Adapter(promiseArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }
}
