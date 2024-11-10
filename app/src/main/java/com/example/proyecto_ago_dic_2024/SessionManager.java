package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserData(JSONObject userData) {
        try {
            editor.putString("user_id", userData.getString("idUser"));
            editor.putString("user_name", userData.getString("name"));
            editor.putString("user_email", userData.getString("email"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains("user_id");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
