package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.PromisePlayer;
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
    private LinearLayout players;
    private FirebaseDatabase database, database2;
    private DatabaseReference databaseReference, databaseReference2;
    private String rid;
    private String UID;
    @Nullable
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ValueEventListener promiseSettingListener, promisePointListener, promiseArrivalListener, promiseVoteListener, mapOnMyFriendListener;
    private PromisePlayer promisePlayer_me;
    private int num;
    private ArrayList<Marker> marks;


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        marks = new ArrayList<>();


        //광고 뷰 추가
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //네이버 지도
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        people_number = findViewById(R.id.tv_room_people_count);
        room_name = findViewById(R.id.tv_room_promise);
        players = findViewById(R.id.player_list_lo);
        reach_location = findViewById(R.id.reach_location_btn);

        rid = getIntent().getStringExtra("rid");
        UID = getIntent().getStringExtra("UID");
        num = -1;

        //방세팅
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Promise").child(rid);
        promiseSettingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Promise p = dataSnapshot.getValue(Promise.class);
                assert p != null;
                people_number.setText(String.valueOf(p.getNumOfPlayer()));
                room_name.setText(p.getPromiseName());
                for (PromisePlayer i : p.getPromisePlayer()) {
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText(i.getNickName());
                    tv.setTextSize(15);
                    tv.setGravity(1);
                    players.addView(tv);
                }
                if(p.getVote() != 0){

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

                //객체에서 내 PromisePlayer 객체 찾기
                ArrayList<PromisePlayer> promisePlayers;
                promisePlayers = p.getPromisePlayer();
                num = -1;
                for(int i = 0; i < promisePlayers.size(); i++){
                    if(promisePlayers.get(i).getPlayerUID().equals(UID)){
                        promisePlayer_me = promisePlayers.get(i);
                        num = i;
                    }
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
                        //TODO: if문에 시간 조건도 추가하기
                        Log.v("Map", String.valueOf(distance));
                        if ((distance <= 50.0) && !promisePlayer_me.getArrival()) {
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
                if(hasPermission() && locationManager != null){
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

    //친구위치 맵에 찍어주기
    public void mapOnFriendMark(){
        database2 = FirebaseDatabase.getInstance();
        databaseReference2 = database2.getReference("Promise").child(rid).child("promisePlayer");
        mapOnMyFriendListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(num == -1){
                    return;
                }
                List<HashMap<String, Object>> players = (List<HashMap<String, Object>>) snapshot.getValue();
                Marker temp = new Marker();

                if(marks.size() != 0){
                    for(Marker m : marks){
                        m.setMap(null);
                    }
                }
                marks.clear();

                for(int i = 0; i < players.size(); i++){
                    if(players.get(i).get("playerUID").equals(UID)) {
                        continue;
                    }
                    else if(players.get(i).get("x") instanceof Long && players.get(i).get("y") instanceof Long){
                        continue;
                    }
                    else{
                        double x = (Double) players.get(i).get("x");
                        double y = (Double) players.get(i).get("y");
                        temp.setPosition(new LatLng(y, x));
                        temp.setMap(naverMap);
                        marks.add(temp);
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
                int max = 0;
                for(HashMap<String, Object> hm : promisePlayers){
                    int curr = ((Long) hm.get("ranking")).intValue();
                    if(curr > max){
                        max = curr;
                    }
                }
                max += 1;
                mDatabase.child("Promise").child(rid).child("promisePlayer").child(String.valueOf(num)).child("ranking").setValue(max);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Map", String.valueOf(databaseError.toException()));
            }
        };
        databaseReference.addListenerForSingleValueEvent(promiseArrivalListener);
    }

    //투표 시작 함수
    private void start_vote(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        databaseReference = database.getReference("Promise").child(rid).child("vote");
        promiseVoteListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int vote = snapshot.getValue(Integer.class);
                mDatabase.child("Promise").child(rid).child("vote").setValue(1);
                databaseReference2.removeEventListener(mapOnMyFriendListener);
                locationManager.removeUpdates(locationListener);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(promiseVoteListener);
    }

    //투표 버튼 누르면 생기는 주의 다이얼로그
    public void show_alert_dial(){
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Map.this);
        //alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("주의");
        myAlertBuilder.setMessage("정말로 방 삭제 투표를 시작 할까요?" + "\n" + "시작 후엔 투표가 끝이 날 때까지 방의 기능을 이용할 수 없습니다.");
        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("시작",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                start_vote();
            }
        });
        myAlertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        myAlertBuilder.show();
    }

    //투표 시작 버튼이 눌리면
    public void btn_vote_start(View view){
        if(num == -1){
            Toast.makeText(getApplicationContext(), "잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        show_alert_dial();
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
       assert locationManager != null;
       databaseReference2.removeEventListener(mapOnMyFriendListener);
       locationManager.removeUpdates(locationListener);
       finish();
    }
}