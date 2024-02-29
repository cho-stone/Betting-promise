package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.PACOsoft.promise_betting.Adapter.History_List_Adapter;
import com.PACOsoft.promise_betting.Adapter.Promise_List_Adapter;
import com.PACOsoft.promise_betting.Adapter.User_List_Adapter;
import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.History;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.User;
import com.PACOsoft.promise_betting.util.ProgressDialog;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;
import java.util.Optional;

public class Home extends AppCompatActivity implements View.OnClickListener, ToolTipsManager.TipListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Promise> promiseArrayList;
    private ArrayList<String> refreshFriendsArrayList, refreshPromisessArrayList;
    private ArrayList<Boolean> unExistFriendsArrayList, unExistPromisesArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2;
    private String TAG, UID;
    private int coin;
    private String me_nickname;
    public static Context context;
    public static ValueEventListener getFriendListValueEventLister, getPromiseListValueEventListener,
            refreshFriendListValueEventLister, refreshFriendListValueEventLister2, refreshFriendListValueEventLister3,
            refreshPromiseListValueEventListener,refreshPromiseListValueEventListener2,refreshPromiseListValueEventListener3;
    public boolean isFriendView;

    //플로팅 버튼
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4, fab5;

    private Button friend_show_button, room_show_button;
    ToolTipsManager toolTipsManager;
    ImageView showCoinImage;
    ConstraintLayout homeRootLayout;
    String current_coin;
    Boolean isCoinShow;

    private com.PACOsoft.promise_betting.util.ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isFriendView = true;
        context = this;
        TAG = "Home";
        coin = -1;
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID"); //mainActivity에서 intent해준 id를 받아옴

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab5 = (FloatingActionButton) findViewById(R.id.fab5);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        fab5.setOnClickListener(this);

        friend_show_button = findViewById(R.id.btnCall);
        room_show_button = findViewById(R.id.btnMessage);

        friend_show_button.setBackground(getDrawable(R.drawable.round_button));
        room_show_button.setBackground(null);

        //코인 툴팁
        toolTipsManager = new ToolTipsManager(this);
        showCoinImage = findViewById(R.id.coin_show_image);
        homeRootLayout = findViewById(R.id.home_root_layout);
        current_coin = "0";
        isCoinShow = false;

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.setCancelable(false); // 로딩창 주변 클릭 시 종료 막기
        //로딩창을 투명하게 하는 코드
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getWindow (): 현재 액티비티의 Window 객체를 가져와서 Window 객체를 통해 뷰들의 위치 크기, 색상 조절
        //Window는 View 의 상위 개념으로, 뷰들을(버튼, 텍스트뷰, 이미지뷰) 감쌓고 있는 컨테이너 역할을 함
        customProgressDialog.show();
        refreshFriendsArrayList = new ArrayList<String>();
        unExistFriendsArrayList = new ArrayList<Boolean>();
        refreshPromisessArrayList = new ArrayList<String>();
        unExistPromisesArrayList = new ArrayList<Boolean>();

        refresh_friends1();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_friends2();
            }
        }, 2000);// 2초 딜레이를 준 후 시작

        refresh_promise1();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_promise2();
            }
        }, 2000);// 2초 딜레이를 준 후 시작

        view_friends();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.dismiss();
            }
        }, 2000);// 2초 딜레이를 준 후 시작
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
        if (coin == -1) {
            Toast.makeText(getApplicationContext(), "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (coin < 100) {
            Toast.makeText(getApplicationContext(), "방 생성 시 최소 100코인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
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
        friend_show_button.setBackground(getDrawable(R.drawable.round_button));
        room_show_button.setBackground(null);
    }

    public void btn_home_promise(View view) {
        view_promise();
        room_show_button.setBackground(getDrawable(R.drawable.round_button));
        friend_show_button.setBackground(null);
    }

    public void btn_promiseClicked(@NonNull View v) {
        TextView tv_promiseKey = v.findViewById(R.id.tv_promiseKey);
        TextView tv_playerNumber = v.findViewById(R.id.tv_numOfPlayer);
        Intent intent = new Intent(this, Map.class);
        intent.putExtra("UID", UID);//ID 정보 intent
        intent.putExtra("rid", tv_promiseKey.getText().toString());
        intent.putExtra("Nop", tv_playerNumber.getText().toString());
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
        if (isFriendView == false)
            databaseReference.removeEventListener(getPromiseListValueEventListener);
        isFriendView = true;
        ValueEventListener getFriendListValueEventLister2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class) == null) {
                    return;
                }
                userArrayList.add(snapshot.getValue(User.class));
                if (!snapshot.getValue(User.class).getId().equals(""))
                    adapter.notifyDataSetChanged();
                else {
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
                current_coin = String.valueOf(coin) + "포인트";

                me_nickname = me.getNickName();

                TextView text2 = (TextView) findViewById(R.id.tv_home_nick_name);
                text2.setText(me_nickname + "님 환영합니다!");

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
        if (isFriendView) databaseReference.removeEventListener(getFriendListValueEventLister);
        isFriendView = false;
        ValueEventListener getPromiseListValueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Promise.class) == null) {
                    return;
                }
                promiseArrayList.add(snapshot.getValue(Promise.class));
                if (!snapshot.getValue(Promise.class).getPromiseKey().equals(""))
                    adapter.notifyDataSetChanged();
                else {
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
                current_coin = String.valueOf(coin) + "포인트";

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

    public void refresh_friends1() {
        unExistFriendsArrayList.clear();
        refreshFriendsArrayList.clear();
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        refreshFriendListValueEventLister2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class) == null) {
                    unExistFriendsArrayList.add(true);
                    return;
                } else unExistFriendsArrayList.add(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        refreshFriendListValueEventLister = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User me = snapshot.getValue(User.class);
                String[] friends = me.getFriendsUID().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                for (int i = 0; i < friends.length; i++) {
                    String friend = friends[i];
                    refreshFriendsArrayList.add(friend);
                    databaseReference2 = database.getReference("User").child(friend);
                    databaseReference2.addListenerForSingleValueEvent(refreshFriendListValueEventLister2);
                    databaseReference2.removeEventListener(refreshFriendListValueEventLister2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(refreshFriendListValueEventLister);
    }

    public void refresh_friends2() {
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        refreshFriendListValueEventLister3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < unExistFriendsArrayList.size(); i++) {
                    if (unExistFriendsArrayList.get(i) == true) {//만약 해당 친구가 더이상 존재하지 않는 유저라면 refreshFriendsArrayList의 해당 index의 값을 ""으로 변경
                        refreshFriendsArrayList.set(i, "");
                    }
                }
                String newMyFriendsString = "";
                for (int i = 0; i < refreshFriendsArrayList.size(); i++) {
                    String str = refreshFriendsArrayList.get(i);
                    if(str.equals("")) {continue;} //만약 존재하지 않는 유저라면 continue로 넘어간다.
                    else newMyFriendsString += str; //아니라면 새로운 친구 목록에 추가해준다.
                    if (i != (refreshFriendsArrayList.size() - 1)) {
                        newMyFriendsString += " ";
                    }
                }
                databaseReference.child("friendsUID").setValue(newMyFriendsString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(refreshFriendListValueEventLister3);
    }

    public void refresh_promise1() {
        unExistPromisesArrayList.clear();
        refreshPromisessArrayList.clear();
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        refreshPromiseListValueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class) == null) {
                    unExistPromisesArrayList.add(true);
                    return;
                } else {
                    unExistPromisesArrayList.add(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        refreshPromiseListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User me = snapshot.getValue(User.class);
                String[] promises = me.getPromiseKey().split(" ");//위에서 필터링한 객체의 FriendsId를 공백을 기준으로 스플릿 해서 배열에 저장
                for (String promise : promises) {
                    refreshPromisessArrayList.add(promise);
                    databaseReference2 = database.getReference("Promise").child(promise);
                    databaseReference2.addListenerForSingleValueEvent(refreshPromiseListValueEventListener2);
                    databaseReference2.removeEventListener(refreshPromiseListValueEventListener2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(refreshPromiseListValueEventListener);
    }

    public void refresh_promise2() {
        database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
        databaseReference = database.getReference("User").child(UID);//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
        refreshPromiseListValueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < unExistPromisesArrayList.size(); i++) {
                    if (unExistPromisesArrayList.get(i) == true) {
                        refreshPromisessArrayList.set(i,"");
                    }
                }
                String newMyPromisesString = "";
                for (int i = 0; i < refreshPromisessArrayList.size(); i++) {
                    String str = refreshPromisessArrayList.get(i);
                    if(str.equals("")) {continue;} //만약 존재하지 않는 약속이라면 continue로 넘어간다.
                    else newMyPromisesString += str;//아니라면 새로운 약속 리스트에 추가해준다.
                    if (i != (refreshPromisessArrayList.size() - 1)) {
                        newMyPromisesString += " ";
                    }

                }
                databaseReference.child("promiseKey").setValue(newMyPromisesString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(refreshPromiseListValueEventListener3);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab) {
            anim();
        } else if (id == R.id.fab1) {
            btnOptionClicked(v);
            anim();
        } else if (id == R.id.fab2) {
            btnSearchHistoryClicked(v);
            anim();
        } else if (id == R.id.fab3) {
            btnSearchFriendClicked(v);
            anim();
        } else if (id == R.id.fab4) {
            btnCreateRoomClicked(v);
            anim();
        } else if (id == R.id.fab5) {
            btnCoinsClicked(v);
            anim();
        }
    }

    private void anim() {
        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab5.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            fab5.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab5.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            fab5.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
        isCoinShow = false;
    }

    public void btn_show_coin(View view) {
        if (!isCoinShow) {
            ToolTip.Builder builder = new ToolTip.Builder(this, showCoinImage, homeRootLayout, current_coin, ToolTip.POSITION_BELOW);
            builder.setAlign(ToolTip.ALIGN_RIGHT);
            builder.setBackgroundColor(Color.argb(150, 0, 0, 0));
            builder.setGravity(ToolTip.GRAVITY_CENTER);
            toolTipsManager.show(builder.build());
            isCoinShow = true;
        } else {
            isCoinShow = false;
            toolTipsManager.dismissAll();
        }

    }

    public void btn_help_home(View view){
    }
}