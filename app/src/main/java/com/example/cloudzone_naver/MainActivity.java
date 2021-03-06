package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.cloudzone_naver.Adapder.pointAdapter;
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

    public List<PostItem> nonsmoking = new ArrayList<>();

    public List<PostItem> nonSmoking_list = new ArrayList<>();
    public List<smokingItem> smoking_list = new ArrayList<>();
    // public List<mannerAreaItem> manner_list = new ArrayList<>();

    public List<CircleOverlay> nonSmokingCircle = new ArrayList<>();
    public List<CircleOverlay> smokingCircle = new ArrayList<>();
    // public List<mannerAreaItem> mannerPoly= new ArrayList<>();

    public List<Marker> nonSmokingMarkers = new ArrayList<>();
    public List<Marker> smokingMarkers= new ArrayList<>();

    public List<nonSmoke> nonSmokeAreas = new ArrayList<>();
    public List<smoke> smokeAreas = new ArrayList<>();
    //public List<InfoWindow> nonSmokeAreaInfos = new ArrayList<>();

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
        ToggleButton btn_plus = (ToggleButton) findViewById(R.id.btn_plus);
        ToggleButton btn_progress = (ToggleButton) findViewById(R.id.progress);
        ProgressBar bar = findViewById(R.id.progressBar);



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

                                    //????????? ?????????
                                    circle.setOnClickListener(new Overlay.OnClickListener() {
                                        @Override
                                        public boolean onClick(@NonNull Overlay overlay) {
                                            ViewGroup rootView = (ViewGroup)findViewById(R.id.map_view);
                                            System.out.println("name :"+item.getName());

                                            pointAdapter adapter = new pointAdapter(MainActivity.this, rootView,item.getName(),item.getAddress_doromyung(),"?????? "+item.getFine(),item.getImage());

                                            InfoWindow i = new InfoWindow();
                                            i.setAdapter(adapter);

                                            i.setPosition(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
                                           // TextView location = findViewById(R.id.window_location);
                                            //location.setText("??????????????????");
                                            //????????? ??????
                                            i .setAlpha(0.9f);
                                            //????????? ??????
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
                                                Log.d(TAG, ""+"??? ?????????");
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

                                            pointAdapter adapter = new pointAdapter(MainActivity.this, rootView,item.getName(),item.getAddress_doromyung(),"????????????????????????",item.getImage());
                                            InfoWindow i = new InfoWindow();
                                            i.setAdapter(adapter);
                                            i.setPosition(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
                                            // TextView location = findViewById(R.id.window_location);
                                            //location.setText("??????????????????");
                                            //????????? ??????
                                            i .setAlpha(0.9f);
                                            //????????? ??????
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
                    Call<List<mannerAreaItem>> getCall = mMyAPI3.get_posts();

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
                                    Log.d(TAG,"fffff"+strAry[1]+" "+strAry[2]);

                                    Marker m = new Marker();
                                    m.setPosition(new LatLng(Double.parseDouble(strAry[2]),Double.parseDouble(strAry[1])));
                                    m.setWidth(60);
                                    m.setHeight(60);
                                    m.setMap(NaverMap);

                                    m.setIcon(OverlayImage.fromResource(R.drawable.cloud2));



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

                    for(Marker m : mannerAreaPoint){
                        m.setMap(null);
                    }
                }
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_plus.isChecked()){
                    Log.d(TAG,"GET");
                    Call<List<mannerAreaItem>> getCall = mMyAPI3.get_posts();
                    getCall.enqueue(new Callback<List<mannerAreaItem>>() {
                        @Override
                        public void onResponse(Call<List<mannerAreaItem>> call, Response<List<mannerAreaItem>> response) {
                            if( response.isSuccessful()){
                                List<mannerAreaItem> mList = response.body();
                                for( mannerAreaItem item : mList){
                                    String[] strAry=item.getGeom().split(" ");
                                    strAry[1] = strAry[1].replace("((","");
                                    strAry[strAry.length-1] = strAry[strAry.length-1].replace("))","");
                                    PolygonOverlay polygon = new PolygonOverlay();
                                    List<LatLng> coords = new ArrayList<>();
                                    for(int i=1;i<strAry.length-1;i++){
                                        if(i%2==0){
                                            strAry[i] = strAry[i].replace(",","");
                                        }
                                    }
                                    for(int i=1;i<strAry.length-1;i+=2){
                                        strAry[i+1]=strAry[i+1].replace(")","");
                                        strAry[i+1]=strAry[i+1].replace("(","");
                                        strAry[i]=strAry[i].replace(")","");
                                        strAry[i]=strAry[i].replace("(","");
                                        Double lat=Double.parseDouble(strAry[i+1]);
                                        Double longitude=Double.parseDouble(strAry[i]);
                                        try{
                                            coords.add(new LatLng(lat,longitude));
                                        }catch (Exception e){
                                            Log.d(TAG, "manner strict ??????!!!!!");
                                        }
                                    }

                                    Log.d(TAG, " " + coords.size());
                                    polygon.setCoords(coords);
                                    polygon.setColor(Color.argb(0.8f,0.0f,0.5f,0.9f));
                                    mannerArea.add(polygon);
                                    Log.d(TAG, "manner strict!");
                                    polygon.setMap(NaverMap);
                                }
                            }
                            else {
                                Log.d(TAG,"Status Code : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<mannerAreaItem>> call, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                        }
                    });
                }
                else{
                    for(PolygonOverlay c : mannerArea){
                        c.setMap(null);
                    }
                }
            }
        });

        btn_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowValue = bar.getProgress();
                int maxValue = bar.getMax();
                if(maxValue == nowValue) {
                    nowValue = 0;
                } else {
                    nowValue+=10;
                    bar.setVisibility(View.VISIBLE);

                }
                bar.setProgress(nowValue);
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
            if (!locationSource.isActivated()) { // ?????? ?????????
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
// ?????? ?????? NaverMap ???????????? ??????
        UiSettings uiSettings = NaverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(false);

        // ?????? ?????? ?????????????????? ?????? ->
        //  naverMap.setMapType(NaverMap.MapType.Satellite);
        // ?????? setlocation
         naverMap.setLocationSource(locationSource);
        //????????? ?????? ???????????? ?????????
         naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

       // CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(37.542153, 127.082076));
        //naverMap.moveCamera(cameraUpdate);
    }


}