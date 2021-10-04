package com.example.doanmp3.Service;

public class APIService {

    public static DataService getService(){
        String base_url = "https://tiendung352001.000webhostapp.com/Server/";
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }

    public static DataService getUserService(){
        String user_url = "https://tiendung352001.000webhostapp.com/Client/";
        return APIRetrofitClient.getclient(user_url).create(DataService.class);
    }

    public static NewDataService newService(){
        String user_url = "https://tiendung352001.000webhostapp.com/server-2/";
        return APIRetrofitClient.getclient(user_url).create(NewDataService.class);
    }
}
