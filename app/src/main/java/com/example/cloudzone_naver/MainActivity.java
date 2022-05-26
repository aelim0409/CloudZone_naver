package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static NaverMap naverMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView)findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();

    }
    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        this.naverMap = naverMap;

        // 현재 위치 버튼 보이게 설정
        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(true);

        // 지도 유형 위성사진으로 설정 -> 잘 안나옴
        //naverMap.setMapType(NaverMap.MapType.Satellite);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}