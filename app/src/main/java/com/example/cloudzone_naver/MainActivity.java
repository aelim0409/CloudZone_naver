package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private MapView mapView;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private TextView tv_outPut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.map_view);
        Button btn_non_smoke = findViewById(R.id.btn_non_smoke);
        Button btn_smoke = findViewById(R.id.btn_smoke);
        Button btn_cloud = findViewById(R.id.btn_cloud);
        Button btn_analysis = findViewById(R.id.btn_analysis);



        tv_outPut= findViewById(R.id.textView);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


    btn_non_smoke.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }


    });

        btn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });

        btn_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });

        btn_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Analysis.class);
                startActivity(intent);
            }


        });

    }
    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {

        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        naverMap.setLightness(0.3f);
// 현재 위치 버튼 안보이게 설정
        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(true);

        // 지도 유형 위성사진으로 설정 ->
      //  naverMap.setMapType(NaverMap.MapType.Satellite);
        // 위치 setlocation
        naverMap.setLocationSource(locationSource);
        //트래킹 모두 카메라가 따라감
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }



}