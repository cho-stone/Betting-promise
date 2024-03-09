package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.History;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.PromisePlayer;
import com.PACOsoft.promise_betting.obj.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static NaverMap naverMap;
    private static final int PERMISSION_REQUEST_CODE = 100; // 권한 부여 코드 생성
    private FusedLocationSource locationSource; //현재 위치를 나타내줄 로케이션 소스
    private static final String[] PERMISSIONS = { //위치 권한을 담은 배열
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private DrawerLayout drawerLayout;
    private View drawerView;
    private TextView people_number, room_name, reach_location;
    private LinearLayout players, arrivalPlayers;
    private FirebaseDatabase database, database2, database3;
    private DatabaseReference databaseReference,databaseReference1, databaseReference2,databaseReference3, databaseReference4, databaseReference5;
    private String rid;
    private String UID;
    private String Nop;
    @Nullable
    private LocationManager locationManager;
    private LocationListener locationListener; //상시 리스너
    private ValueEventListener  mapOnMyFriendListener; //상시 리스너
    private ValueEventListener promisePointListener, promiseDeleteListener,promiseSettingListener,  promiseArrivalListener, PointReceiveListener,addHistoryListener, menuSettingListner;//싱글 벨류 리스너
    private PromisePlayer promisePlayer_me;
    private int num;
    private ArrayList<Marker> marks;
    private Betting_Promise bettingPromise;

    private int myRanking;

    private int myReceivePoint;

    private int pYear,pMonth,pDay,pHour,pMinute,NumOfPlayers;
    private String NameOfPromise;
    public static int allBettingMoney = 0; // 배팅금액 총합

    //drawer에 띄울 플레이어 프로필 변수
    private ImageView mapPlayersImg;
    private TextView mapPlayersTxt, mapArrivalTxt;
    private Typeface tf;

    //날짜 비교 - 도착버튼 활성화에 영향
    private boolean isToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        myRanking = 0;
        myReceivePoint = 0;

        marks = new ArrayList<>();

        //네이버 지도
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        people_number = findViewById(R.id.tv_room_people_count);
        room_name = findViewById(R.id.tv_room_promise);
        players = findViewById(R.id.player_list_lo);
        arrivalPlayers = findViewById(R.id.arrival_list_lo);
        reach_location = findViewById(R.id.reach_location_btn);

        rid = getIntent().getStringExtra("rid");
        UID = getIntent().getStringExtra("UID");
        Nop = getIntent().getStringExtra("Nop");
        num = -1;

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        tf = ResourcesCompat.getFont(this, R.font.kingsejong);

        //약속 종료된 방인지 체크(약속 시간 이후 15분 경과 시 방 삭제 필요)
        database = FirebaseDatabase.getInstance();
        databaseReference1 = database.getReference("Promise").child(rid);
        promiseDeleteListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Promise p = dataSnapshot.getValue(Promise.class);
                assert p != null;

                String dateArr[];
                String timeArr[];
                String date = p.getDate();
                String time = p.getTime();
                dateArr = date.split(" ");
                timeArr = time.split(" ");
                pYear = Integer.parseInt(dateArr[0]);
                pMonth = Integer.parseInt(dateArr[1]);
                pDay = Integer.parseInt(dateArr[2]);
                pHour = Integer.parseInt(timeArr[0]);
                pMinute = Integer.parseInt(timeArr[1]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime setTime = LocalDateTime.of(pYear, pMonth, pDay, pHour, pMinute);
                    if (now.isAfter(setTime) && ChronoUnit.MINUTES.between(now, setTime) <= -15) {
                        database.getReference("Promise").child(rid).removeValue();//DB에서 방 삭제
                        //Map의 모든 상시 리스너 종료
                        if(locationListener != null){
                            locationManager.removeUpdates(locationListener);
                        }
                        if(databaseReference2 != null){
                            databaseReference2.removeEventListener(mapOnMyFriendListener);
                        }

                        Toast.makeText(getApplicationContext(), "만료된 약속입니다.", Toast.LENGTH_LONG).show();
                        finish();//만료된 약속인 것을 알리고 Map에서 나감
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Map", String.valueOf(databaseError.toException()));
            }
        };
        databaseReference1.addListenerForSingleValueEvent(promiseDeleteListener);


        //방세팅
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Promise").child(rid);
        promiseSettingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Promise p = dataSnapshot.getValue(Promise.class);
                assert p != null;
                NumOfPlayers = p.getNumOfPlayer();
                NameOfPromise = p.getPromiseName();
                people_number.setText(String.valueOf(p.getNumOfPlayer()));
                room_name.setText(p.getPromiseName());

                //도착버튼 날짜 비교
                isToday = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate now = LocalDate.now();
                    String[] date_ = p.getDate().toString().split(" ");
                    int[] temp_date = new int[3];
                    temp_date[0] = Integer.parseInt(date_[0]);
                    temp_date[1] = Integer.parseInt(date_[1]);
                    temp_date[2] = Integer.parseInt(date_[2]);
                    LocalDate setTime = LocalDate.of(temp_date[0], temp_date[1], temp_date[2]);

                    if(setTime.isEqual(now)) {
                        isToday = true;
                    }
                    else {
                        isToday = false;
                    }


                }

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); //인플레이터에 레이아웃 추가
                players.removeAllViews();

                for (PromisePlayer i : p.getPromisePlayer()) {
                    databaseReference3 = database.getReference("User").child(i.getPlayerUID());
                    menuSettingListner = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            LinearLayout ln = (LinearLayout) inflater.inflate(R.layout.map_list_item, null, false);
                            mapPlayersImg = ln.findViewById(R.id.map_profile);
                            mapPlayersTxt = ln.findViewById(R.id.map_nickname);
                            Glide.with(mapPlayersImg).load(user.getProfile()).into(mapPlayersImg);
                            mapPlayersTxt.setText(user.getNickName());
                            players.addView(ln);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Drawer", String.valueOf(error.toException()));
                        }
                    };
                    databaseReference3.addListenerForSingleValueEvent(menuSettingListner);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Map", String.valueOf(databaseError.toException()));
            }
        };
        databaseReference.addListenerForSingleValueEvent(promiseSettingListener);
    }

    public void room_menu(View view) {
        drawerLayout.openDrawer(drawerView);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); //현재 위치 반영
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        //지도에 도착 마커와 범위 찍기
        databaseReference = database.getReference("Promise").child(rid);
        promisePointListener = new ValueEventListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Promise p = dataSnapshot.getValue(Promise.class);
                assert p != null;
                String[] location_xy = p.getPromisePlace().split(" ");
                double x = Double.parseDouble(location_xy[0]);
                double y = Double.parseDouble(location_xy[1]);

                Marker locat = new Marker();
                locat.setPosition(new LatLng(y, x));
                locat.setMap(naverMap);

                CircleOverlay circle = new CircleOverlay();
                circle.setCenter(new LatLng(y, x));
                circle.setRadius(50);
                circle.setColor(Color.argb(70, 153, 232, 174));
                circle.setOutlineWidth(5);
                circle.setOutlineColor(Color.argb(70, 0, 0, 0));
                circle.setMap(naverMap);

                Map.allBettingMoney = p.getbettingMoney(); //매번 총배팅금액 가져와서 전역변수에 넣어줌

                //객체에서 내 PromisePlayer 객체 찾기
                ArrayList<PromisePlayer> promisePlayers;
                promisePlayers = p.getPromisePlayer();
                num = -1;
                for(int i = 0; i < promisePlayers.size(); i++){
                    if(promisePlayers.get(i).getPlayerUID().equals(UID)){
                        promisePlayer_me = promisePlayers.get(i);
                        num = i;
                        break;
                    }
                }

                // 방의 배팅머니가 0이면 팝업창 띄우기
                if(p.getbettingMoney() == 0){
                    bettingPromise = new Betting_Promise(Map.this, rid, UID, Nop);
                    bettingPromise.show();
                }

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                //실시간 위치 비교 시작
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Location A = new Location("point A");
                        A.setLatitude(location.getLatitude());
                        A.setLongitude(location.getLongitude());

                        Location B = new Location("point B");
                        B.setLatitude(y);
                        B.setLongitude(x);

                        double distance = A.distanceTo(B);
                        if ((distance <= 50.0) && !promisePlayer_me.getArrival() && isToday) {
                            reach_location.setEnabled(true);
                        }
                        else{
                            reach_location.setEnabled(false);
                        }

                        if(promisePlayer_me != null){
                            mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(num)).child("x").setValue(A.getLongitude());
                            mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(num)).child("y").setValue(A.getLatitude());
                        }
                    }
                };
                //퍼미션여부, 로케이션매니저가 담겼는지, 배팅이 완료되었는지, 관전자가 아닌지
                if(hasPermission() && locationManager != null && p.getbettingMoney() != 0 && promisePlayer_me.getBettingMoney() >= 100){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0.5f, locationListener);//5초마다, 50cm움직이면 갱신
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Map", String.valueOf(databaseError.toException()));
            }
        };
        databaseReference.addListenerForSingleValueEvent(promisePointListener);

        mapOnFriendMark();
    }

    @SuppressLint("MissingPermission")
    public void voteComplete(){
        if(hasPermission() && locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0.5f, locationListener);//5초마다, 50cm움직이면 갱신
        }
    }

    //친구위치 맵에 찍어주기, 도착한 사람 누군지 찍어주기
    public void mapOnFriendMark(){
        database2 = FirebaseDatabase.getInstance();
        databaseReference2 = database2.getReference("Promise").child(rid).child("promisePlayer");
        mapOnMyFriendListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(num == -1){
                    return;
                }
                //VoteStart(); //num이 정해진 시점에 VoteStart를 실행해서 num을 -1을 넘기지 않게 방지함
                List<HashMap<String, Object>> players = (List<HashMap<String, Object>>) snapshot.getValue();
                Marker temp = new Marker();

                if(marks.size() != 0){
                    for(Marker m : marks){
                        m.setMap(null);
                    }
                }
                marks.clear();
                if(players != null) {//NullPointerException 방지
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).get("playerUID").equals(UID)) {
                            continue;
                        } else if (players.get(i).get("x") instanceof Long && players.get(i).get("y") instanceof Long) {
                            continue;
                        } else {
                            double x = (Double) players.get(i).get("x");
                            double y = (Double) players.get(i).get("y");
                            temp.setPosition(new LatLng(y, x));
                            temp.setIconTintColor(Color.BLUE);
                            temp.setMap(naverMap);
                            marks.add(temp);
                        }
                    }
                }
                arrivalPlayers.removeAllViews();
                //도착한 사람 구분 해주기
                for(int i = 0; i < players.size(); i++){
                    if((boolean) players.get(i).get("arrival")){
                        mapArrivalTxt = new TextView(getApplicationContext());
                        mapArrivalTxt.setText(players.get(i).get("nickName").toString());
                        mapArrivalTxt.setGravity(Gravity.CENTER);
                        mapArrivalTxt.setTextSize(15);
                        mapArrivalTxt.setTypeface(tf);
                        mapArrivalTxt.setTextColor(Color.BLACK);
                        arrivalPlayers.addView(mapArrivalTxt);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Map", String.valueOf(error.toException()));
            }
        };
        databaseReference2.addValueEventListener(mapOnMyFriendListener);

    }

    //locationManager 퍼미션
    private boolean hasPermission() {
        return PermissionChecker.checkSelfPermission(this, PERMISSIONS[0])
                == PermissionChecker.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, PERMISSIONS[1])
                == PermissionChecker.PERMISSION_GRANTED;
    }

    //트래킹을 위한 퍼미션
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // request code와 권한 획득 여부 확인
        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    //플로팅 버튼 트래킹 모드 재활성화
    public void select_target(View view){
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    //도착했을 때
    public void btn_reach_place(View view) {
        if(num == -1){
            Toast.makeText(getApplicationContext(), "잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(promisePlayer_me.getArrival()){
            Toast.makeText(getApplicationContext(), "이미 도착 하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        databaseReference = database.getReference("Promise").child(rid).child("promisePlayer");
        promiseArrivalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> promisePlayers = (List<HashMap<String, Object>>) dataSnapshot.getValue();

                //도착하면 arrival true로 바꿔주기
                promisePlayer_me.setArrival(true);
                mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(num)).child("arrival").setValue(true);
                Toast.makeText(getApplicationContext(), "도착!", Toast.LENGTH_SHORT).show();

                //랭킹 정해주기
                myRanking = 0;
                for(HashMap<String, Object> hm : promisePlayers){
                    int curr = ((Long) hm.get("ranking")).intValue();
                    if(curr > myRanking){
                        myRanking = curr;
                    }
                }
                myRanking += 1;
                mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(num)).child("ranking").setValue(myRanking);
                //랭킹에 따라서 포인트 지급 1등은 총 배팅 금액의 3/6 2등은 2/6 3등은 1/6을 지급 받음
                myReceivePoint = 0;
                if(myRanking == 1)//1등
                {
                    myReceivePoint = (Map.allBettingMoney/2);
                    History history = new History();
                    String date="";
                    date += String.valueOf(pYear);
                    date += ".";
                    date += String.valueOf(pMonth);
                    date += ".";
                    date += String.valueOf(pDay);
                    date += " ";
                    date += String.valueOf(pHour);
                    date += ":";
                    date += String.valueOf(pMinute);
                    history.setDate(date);
                    history.setNumOfPlayer(NumOfPlayers);
                    history.setPrizeMoney(Map.allBettingMoney);
                    history.setPromiseKey(rid);
                    history.setPromiseName(NameOfPromise);
                    databaseReference5 = database.getReference("History").child(rid);
                    databaseReference5.setValue(history);
                }
                else if(myRanking == 2)//2등
                {
                    myReceivePoint = (Map.allBettingMoney/3);
                }
                else if(myRanking == 3)//3등
                {
                    myReceivePoint = (Map.allBettingMoney/6);
                }
                Toast.makeText(getApplicationContext(), String.valueOf(myReceivePoint), Toast.LENGTH_SHORT).show();
                DatabaseReference mDatabase2;
                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                databaseReference4 = database.getReference("User").child(UID).child("account");

                PointReceiveListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int myBeforePoint = dataSnapshot.getValue(Integer.class);

                        //기존 내 포인트에 새로 얻은 포인트를 합산 후 DB에 저장
                        myBeforePoint += myReceivePoint;
                        mDatabase2.child("User").child(UID).child("account").setValue(myBeforePoint);
                        Toast.makeText(getApplicationContext(), "포인트 획득 완료!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Map", String.valueOf(databaseError.toException()));
                    }
                };
                databaseReference4.addListenerForSingleValueEvent(PointReceiveListener);
                databaseReference5 = database.getReference("User").child(UID).child("historyKey");
                addHistoryListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String myHistory = dataSnapshot.getValue(String.class);

                        //기존 내 히스토리에 새 히스토리 추가
                        if(myHistory!=null)
                        { myHistory += " ";}
                        myHistory += rid;
                        mDatabase2.child("User").child(UID).child("historyKey").setValue(myHistory);
                        Toast.makeText(getApplicationContext(), "히스토리 생성 완료!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Map", String.valueOf(databaseError.toException()));
                    }
                };
                databaseReference5.addListenerForSingleValueEvent(addHistoryListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Map", String.valueOf(databaseError.toException()));
            }
        };
        databaseReference.addListenerForSingleValueEvent(promiseArrivalListener);

    }

    public void btn_map_help(View view){
        Toast.makeText(getApplicationContext(), "추후에 업데이트 예정입니다.", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, Map_Help.class);
        //startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
       if(locationListener == null && databaseReference2 == null){
           Toast.makeText(getApplicationContext(), "지금은 맵을 종료할 수 없습니다.", Toast.LENGTH_SHORT).show();
           return;
       }
       assert locationManager != null;
       databaseReference2.removeEventListener(mapOnMyFriendListener);
       locationManager.removeUpdates(locationListener);
       finish();
    }


}