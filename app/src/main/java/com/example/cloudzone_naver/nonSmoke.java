package com.example.cloudzone_naver;

public class nonSmoke {
    public String lat,log,r,name,money;

    public nonSmoke(String a, String b, String c, String name, String fine)
    {
        this.lat= a;
        this.log = b;
       this.r =c;
       this.name=name;
       this.money = fine;
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
    public String getName()
    {
        return  name;
    }

    public String getMoney()
    {
        return money;
    }

}
