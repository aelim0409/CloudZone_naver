package com.example.cloudzone_naver;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi_mannerArea {
    @POST("/posts/")
    Call<mannerAreaItem> post_posts(@Body mannerAreaItem post);

    @PATCH("/posts/{pk}/")
    Call<mannerAreaItem> patch_posts(@Path("pk") int pk, @Body mannerAreaItem post);

    @DELETE("/posts/{pk}/")
    Call<mannerAreaItem> delete_posts(@Path("pk") int pk);

    @GET("/")    //매너구역 주소 나오면 넣기
    Call<List<mannerAreaItem>> get_posts();

    @GET("/{pk}/")
    Call<mannerAreaItem> get_post_pk(@Path("pk") int pk);
}
