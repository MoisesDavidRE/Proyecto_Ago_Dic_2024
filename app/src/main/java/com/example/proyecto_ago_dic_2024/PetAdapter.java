package com.example.proyecto_ago_dic_2024;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends ArrayAdapter<Pet> {
    public PetAdapter(Context context, List<Pet> pets) {
        super(context, 0, pets);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pet pet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pet_list_item, parent, false);
        }
        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        ImageView notificationIcon = convertView.findViewById(R.id.notificationView);
        text1.setText(pet.getName());
        text2.setText("Animal: " + pet.getAnimal() + " Raza: " + pet.getBreed());

        Picasso.get().load(pet.getImage1()).into(imageView);

        if (pet.hasPendingRequests()) {
            notificationIcon.setVisibility(View.VISIBLE);
        } else {
            notificationIcon.setVisibility(View.GONE);
        }

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModal(pet.getIdPet());
            }
        });

        return convertView;
    }

    private void showModal(Integer idPet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_requests, null);
        builder.setView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RequestAdapter requestAdapter = new RequestAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(requestAdapter);

        String url = "https://david255311.pythonanywhere.com/get_requests_by_pet/" + idPet;

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Resultados", ""+response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray requestsArray = jsonObject.getJSONArray("requests");
                        List<RequestSolicitudes> requestList = new ArrayList<>();

                        for (int i = 0; i < requestsArray.length(); i++) {
                            JSONObject requestObject = requestsArray.getJSONObject(i);
                            RequestSolicitudes requestSolicitudes = new RequestSolicitudes(
                                    requestObject.getString("city"),
                                    requestObject.getString("description"),
                                    requestObject.getString("lastName1"),
                                    requestObject.getString("lastName2"),
                                    requestObject.getString("message"),
                                    requestObject.getString("name"),
                                    requestObject.getString("socialNetwork"),
                                    requestObject.getString("status"),
                                    requestObject.getString("idRequest")
                            );
                            requestList.add(requestSolicitudes);
                        }
                        requestAdapter.updateRequestList(requestList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });
        requestQueue.add(stringRequest);

        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}