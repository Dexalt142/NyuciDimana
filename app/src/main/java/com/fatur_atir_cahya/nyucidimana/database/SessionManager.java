package com.fatur_atir_cahya.nyucidimana.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefEditor;
    private final String USER_SESSION = "USER_SESSION";

    private final String USER_EMAIL = "USER_EMAIL";
    private final String USER_ROLE = "USER_ROLE";
    private final String USER_TOKEN = "USER_TOKEN";

    public SessionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE);
        this.sharedPrefEditor = sharedPreferences.edit();
    }

    public boolean hasToken() {
        return sharedPreferences.contains(USER_TOKEN);
    }

    public void saveUser(String email, String role, String token) {
        sharedPrefEditor.putString(USER_EMAIL, email);
        sharedPrefEditor.putString(USER_ROLE, role);
        sharedPrefEditor.putString(USER_TOKEN, token);
        sharedPrefEditor.clear();
        sharedPrefEditor.commit();
    }
    public String getToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

}
