package com.test.helloandroid.business.wanandroid.http.service;


import com.test.helloandroid.business.wanandroid.http.bean.WanChapters;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WanAndroidService {

    @GET("wxarticle/chapters/json")
    Call<WanChapters> getChapters();

}
