package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cloudzone_naver.Adapder.pointAdapter;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
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
import com.naver.maps.map.util.FusedLocationSource;

import java.sql.Array;
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
    private NaverMap NaverMap;

    private final String BASE_URL = "http://13.125.51.242:8000/";
    private MyApi mMyAPI;
    private MyApi_smoking mMyAPI2;
    private MyApi_mannerArea mMyAPI3;

    public List<PostItem> nonsmoking = new ArrayList<>();

    public List<PostItem> nonSmoking_list = new ArrayList<>();
    public List<smokingItem> smoking_list = new ArrayList<>();

    public List<CircleOverlay> nonSmokingCircle = new ArrayList<>();
    public List<CircleOverlay> smokingCircle = new ArrayList<>();

    public List<Marker> nonSmokingMarkers = new ArrayList<>();
    public List<Marker> smokingMarkers= new ArrayList<>();

    public List<nonSmoke> nonSmokeAreas = new ArrayList<>();
    public List<smoke> smokeAreas = new ArrayList<>();
    //public List<InfoWindow> nonSmokeAreaInfos = new ArrayList<>();


    public int flag=0;

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
        initMyAPI_mannerArea(BASE_URL);

        btn_non_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                Log.d(TAG, "GET");
                Call<List<PostItem>> getCall = mMyAPI.get_posts();
                getCall.enqueue(new Callback<List<PostItem>>() {
                    @Override
                    public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                        Log.d(TAG, "" + call);
                        Log.d(TAG, "" + response.body());
                        Log.d(TAG, "" + response);
                        if (response.isSuccessful()) {
                            nonSmoking_list = response.body();
                            String result = "";
                            for (PostItem item : nonSmoking_list) {

                                nonSmokeAreas.add(new nonSmoke(item.getLatitude(),item.getLongitude(),item.getRadius(),item.getName(),item.getFine()));
                            }

                            Log.d(TAG, " " + result);
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

                for (nonSmoke i : nonSmokeAreas) {
                    Double Lat=Double.parseDouble(i.getLat());
                    Double Lon=Double.parseDouble(i.getLog());
                    Double r=Double.parseDouble(i.getRadius());

                    int size = (int) Math.round(r);
                    Marker m = new Marker();
                    //원근감 표시
                    m.setIconPerspectiveEnabled(true);
                    //아이콘 지정
                    m.setIcon(OverlayImage.fromResource(R.drawable.ic_redcircle_svg));
                    //마커의 투명도
                    m.setAlpha(1.0f);
                    //마커 위치 circle 위에 생겨 좌표 임의 설정 필요
                    m.setPosition(new LatLng(Lat-r/170000, Lon)); //170000 zoom 15 - > 300000
                    m.setHeight(size);
                    m.setWidth(size);
                    m.setMinZoom(10);
                    m.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {
                            ViewGroup rootView = (ViewGroup)findViewById(R.id.map_view);
                            System.out.println("name :"+i.getName());
                            pointAdapter adapter = new pointAdapter(MainActivity.this, rootView,i.getName(),"dddddd",i.getMoney());

                            InfoWindow i = new InfoWindow();
                            i .setAdapter(adapter);


                            //투명도 조정
                            i .setAlpha(0.9f);
                            //인포창 표시
                            i.open(m);
                            return false;
                        }
                    });


                    //마커 우선순위
                   // m.setZIndex(zIndex);
                    //마커 표시
                    // marker.setMap(naverMap);


                    nonSmokingMarkers.add(m);
                    CircleOverlay circle= new CircleOverlay();
                    circle.setCenter(new LatLng(Lat,Lon));
                    circle.setRadius(5);
                    circle.setColor(Color.RED);

                    //circle.s;

                    nonSmokingCircle.add(circle);



                }

                DrawCircle();
            }
        });

        btn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=2;
                Log.d(TAG,"GET");
                Call<List<smokingItem>> getCall = mMyAPI2.get_posts();
                getCall.enqueue(new Callback<List<smokingItem>>() {
                    @Override
                    public void onResponse(Call<List<smokingItem>> call, Response<List<smokingItem>> response) {
                        if( response.isSuccessful()){
                            smoking_list = response.body();
                            for( smokingItem item : smoking_list){
                                Log.d(TAG, ""+item.getLatitude()+" "+item.getLongitude()+" "+" "+item.getRadius());
                                smokeAreas.add(new smoke(item.getLatitude(),item.getLongitude(),item.getRadius()));
                            }
                            Log.d(TAG,"smoking : success");
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<List<smokingItem>> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });


                for (smoke i : smokeAreas) {
                    Double Lat=Double.parseDouble(i.getLat());
                    Double Lon=Double.parseDouble(i.getLog());
                    Double r=Double.parseDouble(i.getRadius());

                    int size = (int) Math.round(r);
                    Marker m = new Marker();
                    //원근감 표시
                    m.setIconPerspectiveEnabled(true);
                    //아이콘 지정
                    m.setIcon(OverlayImage.fromResource(R.drawable.ic_greencircle_svg));
                    //마커의 투명도
                    m.setAlpha(1.0f);
                    //마커 위치 circle 위에 생겨 좌표 임의 설정 필요
                    m.setPosition(new LatLng(Lat, Lon));
                    m.setHeight(size*2);
                    m.setWidth(size*2);
                    smokingMarkers.add(m);
                    //마커 우선순위
                    // m.setZIndex(zIndex);
                    //마커 표시
                    // marker.setMap(naverMap);



                    CircleOverlay circle= new CircleOverlay();
                    circle.setCenter(new LatLng(Lat,Lon));
                    circle.setRadius(r);
                    circle.setColor(Color.GREEN);
                    smokingCircle.add(circle);
                }

                DrawCircle();

            }
        });


        btn_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"GET");
                Call<List<mannerAreaItem>> getCall = mMyAPI3.get_posts();
                getCall.enqueue(new Callback<List<mannerAreaItem>>() {
                    @Override
                    public void onResponse(Call<List<mannerAreaItem>> call, Response<List<mannerAreaItem>> response) {
                        if( response.isSuccessful()){
                            /*List<mannerAreaItem> mList = response.body();
                            for( mannerAreaItem item : mList){

                            }*/
                            Log.d(TAG,"mannerArea : success");
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<mannerAreaItem>> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
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

    private void initMyAPI_mannerArea(String baseUrl){
        Log.d(TAG,"initMyAPI_smoking : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI3 = retrofit.create(MyApi_mannerArea.class);
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
        //  현재 위치 NaverMap 안보이게 설정
        UiSettings uiSettings = NaverMap.getUiSettings();

        //uiSettings.setLocationButtonEnabled(true);

        // 지도 유형 위성사진으로 설정 ->
        //  naverMap.setMapType(NaverMap.MapType.Satellite);
        // 위치 setlocation
        // naverMap.setLocationSource(locationSource);
        //트래킹 모두 카메라가 따라감
        // naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        CameraPosition cp = new CameraPosition(
                new LatLng(37.551359,127.0742579),
                14);

        NaverMapOptions options = new NaverMapOptions().camera(cp);
        NaverMap.setCameraPosition(cp);
    }

        public void DrawCircle()
        {
            if(flag==1)
            {
                for(CircleOverlay c : nonSmokingCircle)
                {
                    c.setMap(NaverMap);



                }
                for(Marker m : nonSmokingMarkers)
                {
                    m.setMap(NaverMap);
                    /*
                    LocationOverlay l = NaverMap.getLocationOverlay();
                    l.setVisible(true);
                    l.setPosition(m.getPosition());
                    l.setCircleRadius(m.getHeight());
*/

                }

            }
            if(flag==2)
            {

                for(CircleOverlay c : smokingCircle)
                {
                    c.setMap(NaverMap);
                }

/*

                for(Marker m : smokingMarkers)
                {
                    m.setMap(NaverMap);
                }
*/

            }
        }
}