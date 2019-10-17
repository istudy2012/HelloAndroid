package com.test.helloandroid.business.wanandroid.presenter;

import com.test.helloandroid.business.wanandroid.http.bean.WanChapters;
import com.test.helloandroid.business.wanandroid.http.service.WanAndroidService;
import com.test.lib.json.GSONHelper;
import com.test.lib.log.HLog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPresenter {

    private WanAndroidService wanService;

    public static final MediaType JSON
        = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient httpClient;

    public MainPresenter() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        wanService = retrofit.create(WanAndroidService.class);

        httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    public void fetchChapters() {
        Call<WanChapters> call = wanService.getChapters();
        call.enqueue(new Callback<WanChapters>() {
            @Override
            public void onResponse(Call<WanChapters> call, Response<WanChapters> response) {
                HLog.d("test", "response=" + GSONHelper.toString(response.body()));
            }

            @Override
            public void onFailure(Call<WanChapters> call, Throwable t) {
                HLog.d("test", "Throwable=" + t.getMessage());
            }
        });
    }

    public void fetchOther() {
        String url = "https://www.wanandroid.com/wxarticle/chapters/json";
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        okhttp3.Call call = httpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                HLog.d("test", "onFailure=" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                HLog.d("test", "response=" + response.body().string());
            }
        });
    }

}
