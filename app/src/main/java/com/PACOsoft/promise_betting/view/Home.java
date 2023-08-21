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
    private ArrayList<User> userArrayList;
    private ArrayList<Promise> promiseArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String TAG;
    private String UID;
    private int coin;
    private String[] promises;
    private String newPromises;
    private String newFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TAG = "Home";
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID"); //mainActivity에서 intent해준 id를 받아옴
        reFreshData();
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
        //나의 유저 정보 DB에 접근해서 내 약속들 가져오기 시작
        recyclerView = findViewById(R.id.homeRecyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        promiseArrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getUID().equals(UID))) {//myId와 동일한 id가 DB에 있는지 확인
                    Optional<User> me = users.stream().parallel().filter(u -> u.getUID().equals(UID)).findFirst();//User에서 id가 myId와 동일한 객체를 필터링
                    coin = me.get().getAccount();//내 객체에서 account값 가져옴
                    TextView text = (TextView) findViewById(R.id.tv_point);//TextView 참조 객체 선언
                    text.setText(String.valueOf(coin));//위에서 선언한 참조 객체에 값 넘겨줌
                    promises = me.get().getPromiseKey().split(" ");//위에서 필터링한 객체의 PromiseKey를 공백을 기준으로 스플릿 해서 배열에 저장
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e(TAG, String.valueOf(databaseError.toException()));//에러문 출력
            }
        });
        //가져오기 끝

        //약속 정보 DB에 접근해서 내 약속과 일치하는 약속들만 어렙터 연결해서 리사이클러뷰에 띄워주고 newPromises에 저장
        newPromises = "";//newPromises 초기화
        databaseReference = database.getReference("Promise");//DB테이블 연결, 파이어베이스 콘솔에서 History에 접근
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                promiseArrayList.clear(); //기존 배열리스트를 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Promise promise = snapshot.getValue(Promise.class); // 만들어뒀던 promise 객체에 데이터를 담는다
                    for (String temp : promises) {
                        if (promise.getPromiseKey().equals(temp)) {
                            promiseArrayList.add(promise);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                        }
                    }
                }
                adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                //DB의 promise 데이터와 내 promise 데이터의 차이 확인하고 있다면 내 promise 데이터 수정 시작
                database.getReference("User").child(UID).child("promiseKey").setValue(newPromises);
                //수정 끝
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e(TAG, String.valueOf(databaseError.toException()));//에러문 출력
            }

        });
        adapter = new Promise_List_Adapter(promiseArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        //어뎁터 연결 후 리사이클러뷰에 띄우고 newPromises에 저장 끝


    }


    public void btn_promiseClicked(View v) {
        TextView tv_promiseKey = v.findViewById(R.id.tv_promiseKey);
        Intent intent = new Intent(this, Map.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        intent.putExtra("rid", tv_promiseKey.getText().toString());
        startActivity(intent);
    }

    public void view_friends() {
        newFriends = "";//newFriends 초기화
        recyclerView = findViewById(R.id.homeRecyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        userArrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                userArrayList.clear(); //기존 배열리스트를 초기화
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getUID().equals(UID))) {//myId와 동일한 id가 DB에 있는지 확인
                    Optional<User> anyElement = users.stream().parallel().filter(u -> u.getUID().equals(UID)).findFirst(); //User에서 id가 myId와 동일한 객체를 필터링
                    String[] s = anyElement.get().getFriendsId().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                    coin = anyElement.get().getAccount();//내 객체에서 account값 가져옴
                    TextView text = (TextView) findViewById(R.id.tv_point);//TextView 참조 객체 선언
                    text.setText(String.valueOf(coin));//위에서 선언한 참조 객체에 값 넘겨줌
                    for (User user : users) {
                        for (String t : s) {
                            if (user.getId().equals(t)) {
                                userArrayList.add(user);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e(TAG, String.valueOf(databaseError.toException()));//에러문 출력
            }
        });
        adapter = new User_List_Adapter(userArrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }

    public void reFreshData() {
        newFriends = "";//newFriends 초기화
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getUID().equals(UID))) {//myId와 동일한 id가 DB에 있는지 확인
                    Optional<User> anyElement = users.stream().parallel().filter(u -> u.getUID().equals(UID)).findFirst(); //User에서 id가 myId와 동일한 객체를 필터링
                    String[] s = anyElement.get().getFriendsId().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                    coin = anyElement.get().getAccount();//내 객체에서 account값 가져옴
                    TextView text = (TextView) findViewById(R.id.tv_point);//TextView 참조 객체 선언
                    text.setText(String.valueOf(coin));//위에서 선언한 참조 객체에 값 넘겨줌
                    for (User user : users) {
                        for (String t : s) {
                            if (user.getId().equals(t)) {
                                if (newFriends == "" && t != null) {
                                    newFriends = t;
                                } else if (t != null) {
                                    newFriends += " " + t;
                                }
                                break;
                            }
                        }
                    }
                    //DB의 promise 데이터와 내 promise 데이터의 차이 확인하고 있다면 내 promise 데이터 수정 시작
                    database.getReference("User").child(UID).child("friendsId").setValue(newFriends);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e(TAG, String.valueOf(databaseError.toException()));//에러문 출력
            }
        });

        //나의 유저 정보 DB에 접근해서 내 약속들 가져오기 시작
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
                }
                if (users.stream().parallel().anyMatch(u -> u.getUID().equals(UID))) {//myId와 동일한 id가 DB에 있는지 확인
                    Optional<User> me = users.stream().parallel().filter(u -> u.getUID().equals(UID)).findFirst();//User에서 id가 myId와 동일한 객체를 필터링
                    promises = me.get().getPromiseKey().split(" ");//위에서 필터링한 객체의 PromiseKey를 공백을 기준으로 스플릿 해서 배열에 저장
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e(TAG, String.valueOf(databaseError.toException()));//에러문 출력
            }
        });
        //가져오기 끝

        //약속 정보 DB에 접근해서 내 약속과 일치하는 약속들만 어렙터 연결해서 리사이클러뷰에 띄워주고 newPromises에 저장
        newPromises = "";//newPromises 초기화
        databaseReference = database.getReference("Promise");//DB테이블 연결, 파이어베이스 콘솔에서 History에 접근
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Promise promise = snapshot.getValue(Promise.class); // 만들어뒀던 promise 객체에 데이터를 담는다
                    for (String temp : promises) {
                        if (promise.getPromiseKey().equals(temp)) {
                            if (newPromises == "" && temp != null) {
                                newPromises = temp;
                            } else if (temp != null) {
                                newPromises += " " + temp;
                            }
                            break;
                        }
                    }
                }
                //DB의 promise 데이터와 내 promise 데이터의 차이 확인하고 있다면 내 promise 데이터 수정 시작
                database.getReference("User").child(UID).child("promiseKey").setValue(newPromises);
                //수정 끝
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e(TAG, String.valueOf(databaseError.toException()));//에러문 출력
            }

        });


    }
}
