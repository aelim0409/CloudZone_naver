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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

public class Analysis extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analysis);


        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.analysis_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        //광진구로 보여주려고(근데 일단 서울)
        LatLng GwangJin = new LatLng(37.57,126.97);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(GwangJin);
        markerOptions.title("광진");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GwangJin,10));


       // this.mMap = googleMap;
    }
}
