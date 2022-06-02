package com.example.cloudzone_naver;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyApi_smoking{
        @POST("/posts/")
        Call<smokingItem> post_posts(@Body smokingItem post);

        @PATCH("/posts/{pk}/")
        Call<smokingItem> patch_posts(@Path("pk") int pk, @Body smokingItem post);

        @DELETE("/posts/{pk}/")
        Call<smokingItem> delete_posts(@Path("pk") int pk);

        @GET("/smokings/")
        Call<List<smokingItem>> get_posts();

        @GET("/{pk}/")
        Call<smokingItem> get_post_pk(@Path("pk") int pk);
}
