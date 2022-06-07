package com.example.cloudzone_naver;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi_manner_point {
    @POST("/posts/")
    Call<mannerAreaPointItem> post_posts(@Body mannerAreaPointItem post);

    @PATCH("/posts/{pk}/")
    Call<mannerAreaPointItem> patch_posts(@Path("pk") int pk, @Body mannerAreaPointItem post);

    @DELETE("/posts/{pk}/")
    Call<mannerAreaPointItem> delete_posts(@Path("pk") int pk);

    @GET("/mannerpoint/")    //매너구역 주소 나오면 넣기
    Call<List<mannerAreaPointItem>> get_posts();

    @GET("/{pk}/")
    Call<mannerAreaPointItem> get_post_pk(@Path("pk") int pk);
}
