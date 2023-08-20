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
    private DatabaseReference databaseReference;
    private User_List_Adapter adapter;
    private Dialog Add_Friend;
    private Optional<User> anyElement;
    private Optional<User> anyElement2;
    private String temp;
    private String myId;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        Intent intent = getIntent();
        myId = intent.getStringExtra("myId"); //Home에서 intent해준 id를 받아옴
        UID = intent.getStringExtra("UID"); //Home에서 intent해준 UID를 받아옴
    }

    public void btn_SearchFriend(View view) {//검색 버튼 누르면 실행
        recyclerView = findViewById(R.id.friendsRecyclerview); // 아이디 연결
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);//콘텍스트 자동입력
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();// User 객체를 담을 ArrayList(Adapter쪽으로 날릴 것임)
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

                    if (users.stream().parallel().anyMatch(u -> u.getId().equals(textView.getText().toString()))) {//텍스트뷰에서 가져온 텍스트와 동일한 id가 DB에 있는지 확인
                        Toast toast = Toast.makeText(getApplicationContext(), "통과", Toast.LENGTH_SHORT);
                        toast.show();

                        if (users.stream().parallel().anyMatch(u -> u.getUID().equals(UID))) {//UID와 동일한 id가 DB에 있는지 확인
                            Optional<User> me = users.stream().parallel().filter(u -> u.getUID().equals(UID)).findFirst();//User에서 id가 UID와 동일한 객체를 필터링해서 me로 생성
                            if (textView.getText().toString().equals(me.get().getId())) {
                                toast = Toast.makeText(getApplicationContext(), "자기 자신은 추가할 수 없습니다.", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
                            String[] s = me.get().getFriendsId().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다
                                for (String t : s) {
                                    if (user.getId().equals(t) && user.getId().equals(textView.getText().toString())) {//친구 추가 중복 방지 기능 존재하는 친구면 toast띄우고 리턴으로 함수 종료
                                        toast = Toast.makeText(getApplicationContext(), "이미 친구 목록에 존재하는 친구입니다.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        return;
                                    }

                                }
                            }
                            //중복된 친구가 없는 경우에만 추가 가능
                            anyElement = users.stream().parallel().filter(u -> u.getId().equals(textView.getText().toString())).findFirst();
                            anyElement2 = users.stream().parallel().filter(u -> u.getId().equals(myId)).findFirst();
                            //DB에 동일한 ID가 존재한다면 텍스트뷰 참조 객체에서 입력된 텍스트 받아와서 DB의 id와 동일한 객체 찾음
                            String s1 = anyElement.get().getId();
                            String s2 = anyElement2.get().getFriendsId();
                            temp = s2 + " " + s1;
                            //찾은 객체의 id가져와서 스트링에 저장
                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                User friend = snapshot2.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다
                                if (friend.getId().equals(s1)) {
                                    arrayList.add(friend);//담은 데이터를 어레이리스트에 넣고 리사이클러뷰로 보낼 준비함
                                    adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                                    return;
                                }
                            }
                        }


                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "일치하는 ID가 없습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                Log.e("Search_Friend", String.valueOf(databaseError.toException()));//에러문 출력
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
        databaseReference.child(UID).child("friendsId").setValue(temp);
        Add_Friend.cancel();
        Toast toast = Toast.makeText(getApplicationContext(), "친구 추가 완료", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void btn_add_friend_cancel(View view) {//취소 버튼
        Add_Friend.cancel();
    }

}