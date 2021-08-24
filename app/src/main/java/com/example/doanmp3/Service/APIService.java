package com.example.doanmp3.Service;

public class APIService {

    public static DataService getService(){
//        String base_url = "http://192.168.1.3/PlayerMusicProject/Server/Server/";

        String base_url = "http://192.168.1.2/PlayerMusicProject/Server/Server/";
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }

    public static DataService getUserService(){
        String user_url = "http://192.168.1.2/PlayerMusicProject/Server/Client/";
        return APIRetrofitClient.getclient(user_url).create(DataService.class);
    }
}
