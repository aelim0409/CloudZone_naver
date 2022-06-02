package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private final  String TAG = getClass().getSimpleName();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private MapView mapView;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    private final String BASE_URL = "http://13.125.51.242:8000/";
    private MyApi mMyAPI;
    private MyApi_smoking mMyAPI2;

    public List<PostItem> nonsmoking = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_non_smoke = findViewById(R.id.btn_non_smoke);
        Button btn_smoke = findViewById(R.id.btn_smoke);
        Button btn_cloud = findViewById(R.id.btn_cloud);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        initMyAPI(BASE_URL);
        initMyAPI_smoking(BASE_URL);

        btn_non_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,"GET");
                Call<List<PostItem>> getCall = mMyAPI.get_posts();

                getCall.enqueue(new Callback<List<PostItem>>() {
                    @Override
                    public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                        if( response.isSuccessful()){
                            List<PostItem> mList = response.body();
                            for( PostItem item : mList){
                                Log.d(TAG, ""+item.getLatitude()+" "+item.getLongitude()+" "+" "+item.getRadius());
                                nonsmoking.add(new PostItem(item.getLatitude(),item.getLongitude(),item.getRadius()));
                            }
                            Log.d(TAG,"non_smoking : success");
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                        }

                    }


                    @Override

                    public void onFailure(Call<List<PostItem>> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
            }


        });
        btn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d(TAG,"GET");
                Call<List<PostItem>> getCall = mMyAPI2.get_posts();

                getCall.enqueue(new Callback<List<PostItem>>() {
                    @Override
                    public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                        if( response.isSuccessful()){
                            List<PostItem> mList = response.body();
                            for( PostItem item : mList){
                                Log.d(TAG, ""+item.getLatitude()+" "+item.getLongitude()+" "+" "+item.getRadius());
                            }
                            Log.d(TAG,"smoking : success");
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                        }

                    }


                    @Override

                    public void onFailure(Call<List<PostItem>> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
            }


        });
        btn_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });

    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyApi.class);

    }
    private void initMyAPI_smoking(String baseUrl){
        Log.d(TAG,"initMyAPI_smoking : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI2 = retrofit.create(MyApi_smoking.class);
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
        // naverMap.setLocationSource(locationSource);
        //트래킹 모두 카메라가 따라감
        // naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        CameraPosition cp = new CameraPosition(
                new LatLng(37.4,127.075),
                16);

        NaverMapOptions options = new NaverMapOptions().camera(cp);

        CircleOverlay circle = new CircleOverlay();
        circle.setCenter(new LatLng(37.5666102, 126.9783881));
        circle.setRadius(500);
        circle.setColor(Color.GREEN);
        circle.setVisible(true);
        Log.d(TAG,"naverMap : "+naverMap);
        circle.setMap(naverMap);
    }
}