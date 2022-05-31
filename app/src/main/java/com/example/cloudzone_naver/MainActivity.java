package com.example.cloudzone_naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
        tv_outPut= findViewById(R.id.textView);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        String url = "http://13.125.51.242:8000/nonsmokings/";
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();

    btn_non_smoke.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            System.out.println("bbbbbbbbb");

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

    // 연동코드
    public class NetworkTask extends AsyncTask<String, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... strings) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            //Log.w("result: ",s);
            tv_outPut.setText(s);
        }
    }
}