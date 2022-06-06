package com.example.cloudzone_naver.Adapder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.naver.maps.map.overlay.InfoWindow;
import com.example.cloudzone_naver.R;

public class pointAdapter extends InfoWindow.DefaultViewAdapter
{
    private final Context mContext;
    private final ViewGroup mParent;
    public String location;
    public  String locationName;
    public String money;

    public pointAdapter(@NonNull Context context, ViewGroup parent, String location, String locationName, String Money)
    {
        super(context);
        mContext = context;
        mParent = parent;
        this.location=location;
        this.locationName=locationName;
        this.money= Money;
    }


    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow)
    {

/*
        LayoutInflater layoutInFlater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInFlater.inflate(R.layout.info_window, null);


 */

        View view =  LayoutInflater.from(mContext).inflate(R.layout.info_window, mParent, false);

        TextView window_location = view.findViewById(R.id.window_location);
       // ImageView window_location_image =  view.findViewById(R.id.window_location_image);
        TextView window_locationName =  view.findViewById(R.id.window_locationName);

        TextView window_money =  view.findViewById(R.id.window_money);

        System.out.println("A location : "+window_location.getText());
        System.out.println("A locationName: "+window_locationName.getText());
        System.out.println("A money : "+window_money.getText());
        window_location.setText(locationName);
     // window_location_image.setImageResource(R.);
        window_locationName.setText(location);
        window_money.setText("벌금" + money);

        System.out.println("A location2 : "+window_location.getText());
        System.out.println("A locationName2: "+window_locationName.getText());
        System.out.println("A money2 : "+window_money.getText());
       // System.out.println("A money : "+money);
        return view;
    }
}