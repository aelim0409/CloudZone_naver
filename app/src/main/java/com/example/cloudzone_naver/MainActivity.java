package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.cloudzone_naver.Adapder.pointAdapter;
import com.google.android.material.resources.MaterialAttributes;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolygonOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private NaverMap NaverMap;

    private final String BASE_URL = "http://13.125.51.242:8000/";
    private MyApi mMyAPI;
    private MyApi_smoking mMyAPI2;
    private MyApi_mannerArea mMyAPI3;
    private MyApi_manner_point mMyAPI4;


    public List<PostItem> nonSmoking_list = new ArrayList<>();
    public List<smokingItem> smoking_list = new ArrayList<>();

    public List<CircleOverlay> nonSmokingCircle = new ArrayList<>();
    public List<CircleOverlay> smokingCircle = new ArrayList<>();

    public List<PolygonOverlay> mannerArea = new ArrayList<>();
    public List<Marker> mannerAreaPoint = new ArrayList<>();

    public int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton btn_non_smoke = (ToggleButton)findViewById(R.id.btn_non_smoke);
        ToggleButton btn_smoke = (ToggleButton)findViewById(R.id.btn_smoke);
        ToggleButton btn_cloud = (ToggleButton) findViewById(R.id.btn_cloud);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        initMyAPI(BASE_URL);
        initMyAPI_smoking(BASE_URL);
        initMyAPI_mannerArea(BASE_URL);
        initMyAPI_mannerArea_point(BASE_URL);

        btn_non_smoke.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(btn_non_smoke.isChecked()==true){
                    Call<List<PostItem>> getCall = mMyAPI.get_posts();
                    getCall.enqueue(new Callback<List<PostItem>>() {
                        @Override
                        public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                            if (response.isSuccessful()) {
                                nonSmoking_list = response.body();
                                for (PostItem item : nonSmoking_list) {
                                    CircleOverlay circle= new CircleOverlay();
                                    circle.setCenter(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
                                    circle.setRadius(Double.parseDouble(item.getRadius()));
                                    circle.setColor(Color.argb(0.1f,0.9f,0.0f,0.0f));
                                    circle.setMap(NaverMap);

                                    //정보창 띄우기
                                    circle.setOnClickListener(new Overlay.OnClickListener() {
                                        @Override
                                        public boolean onClick(@NonNull Overlay overlay) {
                                            ViewGroup rootView = (ViewGroup)findViewById(R.id.map_view);
                                            System.out.println("name :"+item.getName());

                                            pointAdapter adapter = new pointAdapter(MainActivity.this, rootView,item.getName(),item.getAddress_doromyung(),"벌금 "+item.getFine(),item.getImage());

                                            InfoWindow i = new InfoWindow();
                                            i.setAdapter(adapter);

                                            i.setPosition(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
                                           // TextView location = findViewById(R.id.window_location);
                                            //location.setText("어린이대공원");
                                            //투명도 조정
                                            i .setAlpha(0.9f);
                                            //인포창 표시
                                          //  i.open(NaverMap);
                                            i.setMap(NaverMap);
                                            i.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {
                                                    i.setMap(null);
                                                    return false;
                                                }
                                            });
                                            /*
                                            NaverMap.setOnMapClickListener((coord, point) -> {
                                                i.close();
                                                Log.d(TAG, ""+"맵 누르기");
                                            });
                                            */

                                            return false;
                                        }
                                    });
                                    nonSmokingCircle.add(circle);
                                }
                                Log.d(TAG, "success");
                            } else {
                                Log.d(TAG, "Status Code : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<PostItem>> call, Throwable t) {
                            Log.d(TAG, "Fail msg : " + t.getMessage());
                        }
                    });
                }
                else{
                    for(CircleOverlay c : nonSmokingCircle){
                        c.setMap(null);
                    }
                }
            }
        });

        btn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_smoke.isChecked()==true){
                    Call<List<smokingItem>> getCall = mMyAPI2.get_posts();
                    getCall.enqueue(new Callback<List<smokingItem>>() {
                        @Override
                        public void onResponse(Call<List<smokingItem>> call, Response<List<smokingItem>> response) {
                            if (response.isSuccessful()) {
                                smoking_list = response.body();
                                for (smokingItem item : smoking_list) {
                                    //nonSmokeAreas.add(new nonSmoke(item.getLatitude(),item.getLongitude(),item.getRadius()));
                                    CircleOverlay circle= new CircleOverlay();
                                    circle.setCenter(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
                                    circle.setRadius(Double.parseDouble(item.getRadius()));
                                    circle.setColor(Color.argb(0.3f,0.0f,1.0f,0.0f));
                                    circle.setMap(NaverMap);
                                    circle.setOnClickListener(new Overlay.OnClickListener() {
                                        @Override
                                        public boolean onClick(@NonNull Overlay overlay) {
                                            ViewGroup rootView = (ViewGroup)findViewById(R.id.map_view);
                                            System.out.println("name :"+item.getName());

                                            pointAdapter adapter = new pointAdapter(MainActivity.this, rootView,item.getName(),item.getAddress_doromyung(),"법정지정흡연구역",item.getImage());
                                            InfoWindow i = new InfoWindow();
                                            i.setAdapter(adapter);
                                            i.setPosition(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
                                            // TextView location = findViewById(R.id.window_location);
                                            //location.setText("어린이대공원");
                                            //투명도 조정
                                            i .setAlpha(0.9f);
                                            //인포창 표시
                                            i.setMap(NaverMap);
                                            i.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {
                                                    i.setMap(null);
                                                    return false;
                                                }
                                            });
                                            return false;
                                        }
                                    });
                                    smokingCircle.add(circle);
                                }
                                Log.d(TAG, "success");
                            } else {
                                Log.d(TAG, "Status Code : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<smokingItem>> call, Throwable t) {
                            Log.d(TAG, "Fail msg : " + t.getMessage());
                        }
                    });
                }
                else{
                    for(CircleOverlay c : smokingCircle){
                        c.setMap(null);
                    }
                }
            }
        });
        btn_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Marker m =new Marker();
                //m.setPosition(new LatLng(127.10530723769554, 37.544845489786375));
                if(btn_cloud.isChecked()==true){
                    Log.d(TAG,"GET");
                    //Call<List<mannerAreaItem>> getCall = mMyAPI3.get_posts();

                    Call<List<mannerAreaPointItem>> getCall2 = mMyAPI4.get_posts();
                    getCall2.enqueue(new Callback<List<mannerAreaPointItem>>() {
                        @Override
                        public void onResponse(Call<List<mannerAreaPointItem>> call, Response<List<mannerAreaPointItem>> response) {
                            if( response.isSuccessful()){
                                List<mannerAreaPointItem> mList = response.body();
                                for( mannerAreaPointItem item : mList){
                                    String[] strAry=item.getGeom().split(" ");
                                    strAry[1] = strAry[1].replace("(","");
                                    strAry[strAry.length-1] = strAry[strAry.length-1].replace(")","");
                                    Log.d(TAG,""+strAry[1]+" "+strAry[2]);

                                    Marker m = new Marker();
                                    m.setPosition(new LatLng(Double.parseDouble(strAry[2]),Double.parseDouble(strAry[1])));
                                    m.setWidth(30);
                                    m.setHeight(50);
                                    m.setMap(NaverMap);
                                    mannerAreaPoint.add(m);
                                }
                            }else {
                                Log.d(TAG,"Status Code : " + response.code());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<mannerAreaPointItem>> call, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                        }
                    });

                }
                else{
                    /*for(PolygonOverlay c : mannerArea){
                        c.setMap(null);
                    }*/
                    for(Marker m : mannerAreaPoint){
                        m.setMap(null);
                    }
                }
            }
        });
    }

    private void initMyAPI(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyApi.class);

    }
    private void initMyAPI_smoking(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI2 = retrofit.create(MyApi_smoking.class);
    }
    private void initMyAPI_mannerArea(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI3 = retrofit.create(MyApi_mannerArea.class);
    }
    private void initMyAPI_mannerArea_point(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI4 = retrofit.create(MyApi_manner_point.class);
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
                NaverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        this.NaverMap=naverMap;
        NaverMap.setLightness(0.3f);
// 현재 위치 NaverMap 안보이게 설정
        UiSettings uiSettings = NaverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(true);

        // 지도 유형 위성사진으로 설정 ->
        //naverMap.setMapType(NaverMap.MapType.Satellite);
        // 위치 setlocation
        naverMap.setLocationSource(locationSource);
        //트래킹 모두 카메라가 따라감
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        //CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(37.542153, 127.082076));
        //naverMap.moveCamera(cameraUpdate);
    }
}