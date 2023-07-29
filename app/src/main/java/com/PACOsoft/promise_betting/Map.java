package com.PACOsoft.promise_betting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class Map extends AppCompatActivity implements OnMapReadyCallback
{

    private MapView mapView;
    private static NaverMap naverMap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //네이버 지도
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(33.38, 126.55), // 위치 지정
                9 // 줌 레벨
        );
        naverMap.setCameraPosition(cameraPosition); //변경된 위치 반영
    }
}