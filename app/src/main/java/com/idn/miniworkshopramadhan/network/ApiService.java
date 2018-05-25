package com.idn.miniworkshopramadhan.network;

import com.idn.miniworkshopramadhan.response.ResponseApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("{Location}/{Date}/weekly.json")
    Call<ResponseApi> scheduleSholat
            (@Path("Location") String location,
             @Path("Date") String date);

}
