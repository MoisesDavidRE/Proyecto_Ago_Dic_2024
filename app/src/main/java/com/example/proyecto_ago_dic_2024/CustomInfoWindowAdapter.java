package com.example.proyecto_ago_dic_2024;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Activity mContext;

    public CustomInfoWindowAdapter(Activity context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null);
    }

    private void renderWindowText(Marker marker, View view){

        String snippet2 = marker.getSnippet();
        try {
            JSONObject markerData = new JSONObject(snippet2);
            String name = markerData.getString("name");
            String gender = markerData.getString("gender");
            String imageUrl = markerData.getString("imageUrl");

            TextView tvName = view.findViewById(R.id.info_window_title);
            tvName.setText(name);

            ImageView ivImage = view.findViewById(R.id.info_window_image);
            Picasso.get().load(imageUrl).into(ivImage);
        } catch (JSONException e) { 
            e.printStackTrace();
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}
