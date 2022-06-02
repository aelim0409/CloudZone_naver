package com.example.cloudzone_naver;

public class nonSmoke {
    public String lat,log,r;

    public nonSmoke(String a, String b, String c)
    {
        this.lat= a;
        this.log = b;
        this.r =c;
    }

    public String getLat() {
        return lat;
    }
    public String getLog()
    {
        return log;
    }
    public String getRadius()
    {
        return r;
    }
}
