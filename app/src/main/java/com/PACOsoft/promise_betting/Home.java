package com.PACOsoft.promise_betting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import java.util.Optional;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String myId ;
    private String myPassword ;
    private int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        myId = intent.getStringExtra("myId"); //mainActivity에서 intent해준 id를 받아옴
        myPassword = intent.getStringExtra("myPassword");




        recyclerView = findViewById(R.id.userRecyclerView); // 아이디 연결
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
                    // 람다식 : 델리게이트 -> 일반화(간소화)
                    // 델리게이트 : 함수를 변수처럼 사용하게 해주는 기능
                    // 1회용함수
                    String[] s = anyElement.get().getFriendsId().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                    point = anyElement.get().getAccount();//내 객체에서 account값 가져옴
                    TextView text = (TextView) findViewById(R.id.tv_point);//TextView 참조 객체 선언
                    text.setText(String.valueOf(point));//위에서 선언한 참조 객체에 값 넘겨줌
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다
                        for (String t : s) {
                            if (user.getId().equals(t))
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

    //Home에서 SearchFriend로 이동하는 버튼 구현
    public void btnSearchFriendClicked(View view) {
        Intent intent = new Intent(this, Search_Friend.class);
        startActivity(intent);
    }

    //Home에서 SearchHistory로 이동하는 버튼 구현
    public void btnSearchHistoryClicked(View view) {
        Intent intent = new Intent(this, Search_History.class);
        startActivity(intent);
    }

    //홈에서 유저 눌렀을 때 안팅기게 만듦
    public void btn_UserClicked(View view) {
    }

    public void btnCoinsClicked(View view){
        Intent intent = new Intent(this, Coin.class);
        startActivity(intent);
    }

    public void btnCreateClicked(View view){
        Intent intent = new Intent(this, Create_Room.class);
        startActivity(intent);
    }

    public void btnOptionClicked(View view){
        Intent intent = new Intent(this, Option.class);
        startActivity(intent);
    }
}