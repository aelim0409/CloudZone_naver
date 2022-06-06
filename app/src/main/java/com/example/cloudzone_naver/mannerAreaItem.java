package com.example.cloudzone_naver;

public class mannerAreaItem {
    private String id;
    private String geom;
    private String fid;
    private String dn;

    public String getId(){return id;}
    public String getGeom(){return geom;}
    public String fid(){return fid;}
    public String dn(){return dn;}

    public void setId(String s){
        id=s;
    }
    public void setGeom(String s){geom=s;}
    public void setFid(String s){fid=s;}
    public void setDn(String s){dn=s;}
    //String list 데이터 형태 보고 수정
}
