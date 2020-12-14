package com.fatur_atir_cahya.nyucidimana.api.model;

import com.google.gson.JsonObject;

public class Laundromat {

    private String name;
    private String address;
    private String latitude;
    private String longitude;

    public Laundromat(String name, String address, String latitude, String longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Laundromat(JsonObject jsonObject) {
        this.name = jsonObject.get("name").getAsString();
        this.address = jsonObject.get("address").getAsString();
        this.latitude = jsonObject.get("latitude").getAsString();
        this.longitude = jsonObject.get("longitude").getAsString();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
