package com.fatur_atir_cahya.nyucidimana.api.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TransactionInterface {

    @GET("transaction")
    Call<JsonObject> getTransactions(@Header("Authorization") String authHeader);

    @GET("transaction/{id}")
    Call<JsonObject> getTransaction(@Header("Authorization") String authHeader, @Path("id") String id);

    @GET("transaction/user")
    Call<JsonObject> getUserTransactions(@Header("Authorization") String authHeader);

}
