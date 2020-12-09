package com.fatur_atir_cahya.nyucidimana.api.service;

import com.fatur_atir_cahya.nyucidimana.api.model.Login;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthInterface {

    @POST("auth/login")
    Call<JsonObject> login(@Body Login login);

    @GET("auth/me")
    Call<JsonObject> user(@Header("Authorization") String authHeader);

}
