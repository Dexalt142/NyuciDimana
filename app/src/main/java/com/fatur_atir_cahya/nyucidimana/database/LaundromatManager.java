package com.fatur_atir_cahya.nyucidimana.database;

import android.content.Context;
import android.content.SharedPreferences;

public class LaundromatManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefEditor;
    private final String LAUNDROMAT = "LAUNDROMAT";

    private final String ID = "ID";
    private final String NAME = "NAME";
    private final String ADDRESS = "ADDRESS";
    private final String LATITUDE = "LATITUDE";
    private final String LONGITUDE = "LONGITUDE";

    public LaundromatManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(LAUNDROMAT, Context.MODE_PRIVATE);
        this.sharedPrefEditor = sharedPreferences.edit();
    }

    public void saveLaundromat(String id, String name, String address, String latitude, String longitude) {
        sharedPrefEditor.putString(ID, id);
        sharedPrefEditor.putString(NAME, name);
        sharedPrefEditor.putString(ADDRESS, address);
        sharedPrefEditor.putString(LATITUDE, latitude);
        sharedPrefEditor.putString(LONGITUDE, longitude);
        sharedPrefEditor.clear();
        sharedPrefEditor.commit();
    }

    public boolean hasLaundromat() {
        return sharedPreferences.contains(ID) &&
                sharedPreferences.contains(NAME) &&
                sharedPreferences.contains(ADDRESS) &&
                sharedPreferences.contains(LATITUDE) &&
                sharedPreferences.contains(LONGITUDE);
    }

    public String getId(){
        return sharedPreferences.getString(ID, null);
    }

    public String getName(){
        return sharedPreferences.getString(NAME, null);
    }

    public String getAddress(){
        return sharedPreferences.getString(ADDRESS, null);
    }

    public String getLatitude(){
        return sharedPreferences.getString(LATITUDE, null);
    }

    public String getLongitude(){
        return sharedPreferences.getString(LONGITUDE, null);
    }

}
