package com.test.helloandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.test.helloandroid.business.wanandroid.http.bean.WanChapters;
import com.test.helloandroid.business.wanandroid.http.service.WanAndroidService;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private WanAndroidService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initService();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
            }
        });
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        service = retrofit.create(WanAndroidService.class);
    }

    private void doRequest() {
        Call call = service.getChapters();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("test", "response=" + response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("test", "Throwable=" + t.getMessage());
            }
        });
    }

}
