package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user_doc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserData(JSONObject userData) {
        try {
            editor.putString("user_id", userData.getString("idUser"));
            editor.putString("user_email", userData.getString("email"));
            editor.putString("user_name", userData.getString("name"));
            editor.putString("user_lastName1", userData.getString("lastName1"));
            editor.putString("user_lastName2", userData.getString("lastName2"));
            editor.putString("user_profileImage", userData.getString("profileImage"));
            editor.putString("user_age", userData.getString("userAge"));
            editor.putString("user_state", userData.getString("state"));
            editor.putString("user_city", userData.getString("city"));
            editor.putString("user_socialNetwork", userData.getString("socialNetwork"));
            editor.putString("user_description", userData.getString("description"));
//            Log.d("DatosUsuario", userData.toString());
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
