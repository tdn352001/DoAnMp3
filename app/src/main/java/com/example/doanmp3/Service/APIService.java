package com.example.doanmp3.Service;

public class APIService {
    private static String base_url ="https://tiendung352001.000webhostapp.com/Server/";

    public static DataService getService(){
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }
}
