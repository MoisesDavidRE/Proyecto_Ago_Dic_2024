package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends ArrayAdapter<RequestModel> {
    public UserAdapter(Context context, List<RequestModel> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestModel requestModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }
        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView image1 = convertView.findViewById(R.id.imageView2);
        text2.setText("Estatus: "+ requestModel.getStatus().toUpperCase());
        text1.setText("Mascota: " + requestModel.getPet_name());
        Picasso.get().load(requestModel.getImage1()).into(image1);
        return convertView;
    }
}
