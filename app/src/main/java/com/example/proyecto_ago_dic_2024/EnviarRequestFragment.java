package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EnviarRequestFragment extends Fragment {
    private EditText etRequestMessage;
    private Button btnSendRequest;
    Integer petId;
    String idUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enviar_request, container, false);
        Bundle args = getArguments();
        petId = args.getInt("idPet");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_doc", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("user_id", "Valor por defecto");

        etRequestMessage = view.findViewById(R.id.et_request_message);
        btnSendRequest = view.findViewById(R.id.btn_send_request);

        btnSendRequest.setOnClickListener(v -> sendRequest());
        return view;
    }

    private void sendRequest() {
        String message = etRequestMessage.getText().toString();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Por favor ingresa una descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("pet_id", petId);
            requestData.put("requester_id", Integer.valueOf(idUser));
            requestData.put("message", message);
            requestData.put("status","pending");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://david255311.pythonanywhere.com/pet/request";
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, requestData,
                response -> {
                    Toast.makeText(getContext(), "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show();
                    etRequestMessage.setText("");
                },
                error -> {
                    Toast.makeText(getContext(), "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonRequest);
    }
}