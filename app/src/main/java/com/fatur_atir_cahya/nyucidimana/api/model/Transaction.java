package com.fatur_atir_cahya.nyucidimana.api.model;

import com.google.gson.JsonObject;

public class Transaction {

    private String id;
    private String transactionCode;
    private int weight;
    private int price;
    private String startDate;
    private String endDate;
    private String status;
    private String laundromatId;
    private String userId;
    
    public Transaction() {
        
    }

    public Transaction(String id, String transactionCode, int weight, int price, String startDate, String endDate, String status, String laundromatId, String userId) {
        this.id = id;
        this.transactionCode = transactionCode;
        this.weight = weight;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.laundromatId = laundromatId;
        this.userId = userId;
    }
    
    public void loadFromJson(JsonObject jsonObject) {
        this.id = jsonObject.get("id").getAsString();
        this.transactionCode = jsonObject.get("transaction_code").getAsString();
        this.weight = jsonObject.get("weight").getAsInt();
        this.price = jsonObject.get("price").getAsInt();
        this.startDate = jsonObject.get("start_date").getAsString();
        this.endDate = jsonObject.get("end_date").isJsonNull() ? null : jsonObject.get("end_date").getAsString();
        this.status = jsonObject.get("status").getAsString();
        this.laundromatId = jsonObject.get("laundromat_id").getAsString();
        this.userId = jsonObject.get("user_id").isJsonNull() ? null : jsonObject.get("user_id").getAsString();
    }

    public String getId() {
        return id;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getLaundromatId() {
        return laundromatId;
    }

    public String getUserId() {
        return userId;
    }

}
