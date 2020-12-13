package com.fatur_atir_cahya.nyucidimana.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

public class Transaction implements Parcelable {

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

    protected Transaction(Parcel parcel) {
        this.id = parcel.readString();
        this.transactionCode = parcel.readString();
        this.weight = parcel.readInt();
        this.price = parcel.readInt();
        this.startDate = parcel.readString();
        this.endDate = parcel.readString();
        this.status = parcel.readString();
        this.laundromatId = parcel.readString();
        this.userId = parcel.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(transactionCode);
        parcel.writeInt(weight);
        parcel.writeInt(price);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(status);
        parcel.writeString(laundromatId);
        parcel.writeString(userId);
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
