package com.fatur_atir_cahya.nyucidimana.api.service;

import com.fatur_atir_cahya.nyucidimana.api.model.AttachTransaction;
import com.fatur_atir_cahya.nyucidimana.api.model.CreateTransaction;
import com.fatur_atir_cahya.nyucidimana.api.model.UpdateTransaction;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransactionInterface {

    @GET("transaction")
    Call<JsonObject> getTransactions(@Header("Authorization") String authHeader);

    @GET("transaction/{id}")
    Call<JsonObject> getTransaction(@Header("Authorization") String authHeader, @Path("id") String id);

    @GET("transaction/user")
    Call<JsonObject> getUserTransactions(@Header("Authorization") String authHeader);

    @POST("transaction/create")
    Call<JsonObject> createTransaction(@Header("Authorization") String authHeader, @Body CreateTransaction createTransaction);

    @POST("transaction/update")
    Call<JsonObject> updateTransaction(@Header("Authorization") String authHeader, @Body UpdateTransaction updateTransaction);

    @POST("transaction/user/attach")
    Call<JsonObject> attachTransaction(@Header("Authorization") String authHeader, @Body AttachTransaction attachTransaction);

}
