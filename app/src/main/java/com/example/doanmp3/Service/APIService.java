package com.example.doanmp3.Service;

public class APIService {

    public static DataService getService(){
        String user_url = "https://tiendung352001.000webhostapp.com/server-2/";
        return APIRetrofitClient.getClient(user_url).create(DataService.class);
    }
}
