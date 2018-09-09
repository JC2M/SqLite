package br.com.jc2mit.sqlite.remote;

import retrofit2.Retrofit;

public class APIUtils {

    public APIUtils() {
    };

    public static final String API_URL = "http://192.168.0.55:8080/comercial/";

    public static ClienteService getClienteService(){
        return RetrofitClient.getClient(API_URL).create(ClienteService.class);
    }

}
