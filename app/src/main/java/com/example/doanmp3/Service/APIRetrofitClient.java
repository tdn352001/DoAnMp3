package com.example.doanmp3.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getclient(String base_url){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(15000, TimeUnit.MILLISECONDS)
                                                                .writeTimeout(15000, TimeUnit.MILLISECONDS)
                                                                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                                                                .retryOnConnectionFailure(true)
                                                                 .protocols(Arrays.asList(Protocol.HTTP_1_1)).build();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(base_url);
        builder.client(okHttpClient);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit = builder.build();

        return retrofit;
    }
}
