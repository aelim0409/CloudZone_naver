package com.example.cloudzone_naver.Adapder;

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
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.info_window, mParent, false);

        TextView window_location = (TextView) view.findViewById(R.id.window_location);
       // ImageView window_location_image = (ImageView) view.findViewById(R.id.window_location_image);
        TextView window_locationName = (TextView) view.findViewById(R.id.window_locationName);
        TextView window_money = (TextView) view.findViewById(R.id.window_money);

        System.out.println("A location : "+location);
        window_location.setText("어린이 대공원");
      //  window_location_image.setImageResource(R.drawable.image_point);
        window_locationName.setText(locationName);
        window_money.setText(money);

        return view;
    }
}