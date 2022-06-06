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
    public String img_URL;

    public pointAdapter(@NonNull Context context, ViewGroup parent, String location, String locationName, String Money,String img_url)
    {
        super(context);
        mContext = context;
        mParent = parent;
        this.location=location;
        this.locationName=locationName;
        this.money= Money;
        this.img_URL=img_url;
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
        ImageView window_location_image =  view.findViewById(R.id.window_location_image);
        TextView window_locationName =  view.findViewById(R.id.window_locationName);

        TextView window_money =  view.findViewById(R.id.window_money);

        System.out.println("A location : "+window_location.getText());
        System.out.println("A locationName: "+window_locationName.getText());
        System.out.println("A money : "+window_money.getText());
        window_location.setText(locationName);

        /*
        try{
            Uri uri = Uri.parse(img_URL);
            window_location_image.setImageURI(uri);
        }catch(Exception e){
            e.printStackTrace();
        }


         */
        if(money.equals("법정지정흡연구역"))
            window_location_image.setImageResource(R.drawable.gunja_smoke);
        else
            window_location_image.setImageResource(R.drawable.child_park);
        window_locationName.setText(location);
        window_money.setText(money);

        System.out.println("A location2 : "+window_location.getText());
        System.out.println("A locationName2: "+window_locationName.getText());
        System.out.println("A money2 : "+window_money.getText());
       // System.out.println("A money : "+money);
        return view;
    }
}