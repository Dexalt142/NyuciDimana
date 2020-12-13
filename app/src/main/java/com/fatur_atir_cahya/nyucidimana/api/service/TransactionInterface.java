package com.fatur_atir_cahya.nyucidimana.api.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TransactionInterface {

    @GET("transaction")
    Call<JsonObject> getTransaction(@Header("Authorization") String authHeader);

}
