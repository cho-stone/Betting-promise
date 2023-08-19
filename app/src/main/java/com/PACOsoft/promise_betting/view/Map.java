package com.PACOsoft.promise_betting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.PACOsoft.promise_betting.R;
import com.PACOsoft.promise_betting.obj.Promise;
import com.PACOsoft.promise_betting.obj.PromisePlayer;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.style.layers.LineExtrusionLayer;
import com.naver.maps.map.util.FusedLocationSource;

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
    private Promise promise;
    private TextView people_number, room_name;
    private LinearLayout players;
    private String[] location_xy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //네이버 지도
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

        //객체 가져오기
        promise = (Promise) getIntent().getSerializableExtra("promise");




        people_number = findViewById(R.id.tv_room_people_count);
        room_name = findViewById(R.id.tv_room_promise);
        players = findViewById(R.id.player_list_lo);

        //방설정
        people_number.setText(String.valueOf(promise.getNumOfPlayer()));
        room_name.setText(promise.getPromiseName());
        location_xy = promise.getPromisePlace().split(" ");
        for(PromisePlayer i : promise.getPromisePlayer()){
            TextView tv = new TextView(getApplicationContext());
            tv.setText(i.getNickName());
            tv.setTextSize(15);
            tv.setGravity(1);
            players.addView(tv);
        }
    }

    public void room_menu(View view){
        drawerLayout.openDrawer(drawerView);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); //현재 위치 반영
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        double x = Double.valueOf(location_xy[0]);
        double y = Double.valueOf(location_xy[1]);
        Marker locat = new Marker();
        locat.setPosition(new LatLng(y, x));
        locat.setMap(naverMap);

        CircleOverlay circle = new CircleOverlay();
        circle.setCenter(new LatLng(y, x));
        circle.setRadius(50);
        circle.setColor(Color.argb(70, 153, 232, 174));
        circle.setOutlineWidth(5);
        circle.setOutlineColor(Color.argb(70, 0,0,0));
        circle.setMap(naverMap);
    }

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

    public void target_select(View view){
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    public void btn_reach_place(View view) {
    }

    public void btn_vote_start(View view){
    }
}