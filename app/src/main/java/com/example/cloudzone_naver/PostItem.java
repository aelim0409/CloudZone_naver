package com.example.cloudzone_naver;

public class PostItem {
    private String id;
    private String area_id;
    private String name;
    private String area_detail;
    private String city;
    private String sigungu;
    private String eupmyungdong;
    private String category;
    private String reason;
    private String area_size;
    private String fine;
    private String address_doromyung;
    private String address_jibeon;
    private String manage_office;
    private String latitude;
    private String longitude;
    private String image;
    private String radius;


    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getRadius() {
        return radius;
    }

    public void setLatitude(String s){
        latitude = s;
    }
    public void setLongitude(String s){
        longitude = s;
    }
    public void setRadius(String s){
        radius = s;
    }

}