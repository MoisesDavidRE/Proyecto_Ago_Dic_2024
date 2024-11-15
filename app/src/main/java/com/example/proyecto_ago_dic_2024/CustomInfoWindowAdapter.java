package com.example.proyecto_ago_dic_2024;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Activity mContext;

    public CustomInfoWindowAdapter(Activity context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null);
    }

    private void renderWindowText(Marker marker, View view){
        ImageView imageView = view.findViewById(R.id.info_window_image);
        TextView title = view.findViewById(R.id.info_window_title);
        TextView snippet = view.findViewById(R.id.info_window_snippet);
        Button button = view.findViewById(R.id.info_window_button);

//        imageView.setImageResource(R.drawable.ic_launcher_foreground); // Set your image here
        Picasso.get().load("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSy6A0qhTNrrZ415ug0G27AEh1_FYrzmihMfMKQIVBMqZRRmH6K").into(imageView);
        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
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
