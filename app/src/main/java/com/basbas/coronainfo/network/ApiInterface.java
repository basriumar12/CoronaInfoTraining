package com.basbas.coronainfo.network;

import com.basbas.coronainfo.model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    //endpoint
    @GET("indonesia")
    Call<List<ResponseData>> getData();
}
