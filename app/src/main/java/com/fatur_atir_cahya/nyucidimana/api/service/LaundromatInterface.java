package com.fatur_atir_cahya.nyucidimana.api.service;

import com.fatur_atir_cahya.nyucidimana.api.model.Laundromat;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LaundromatInterface {

    @GET("laundromat/my")
    Call<JsonObject> getLaundromat(@Header("Authorization") String authHeader);

    @GET("laundromat/{id}")
    Call<JsonObject> getLaundromatById(@Header("Authorization") String authHeader, @Path("id") String id);

    @GET("laundromat/nearby/{latitude}/{longitude}")
    Call<JsonObject> getNearbyLaundromat(@Header("Authorization") String authHeader, @Path("latitude") String latitude, @Path("longitude") String longitude);

    @POST("laundromat/create")
    Call<JsonObject> createLaundromat(@Header("Authorization") String authHeader, @Body Laundromat laundromat);

    @GET("laundromat/statistic")
    Call<JsonObject> getStatistics(@Header("Authorization") String authHeader);
}
