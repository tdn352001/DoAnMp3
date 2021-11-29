package com.example.doanmp3.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofitClient {

    public static Retrofit getClient(String base_url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1)).build();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(base_url);
        builder.client(okHttpClient);
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        return builder.build();
    }
}
