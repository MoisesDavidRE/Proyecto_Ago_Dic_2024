package com.example.proyecto_ago_dic_2024;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto_ago_dic_2024.databinding.FragmentOwnerContactBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OwnerContactFragment extends Fragment {
    String url = "https://david255311.pythonanywhere.com/user/";
    Integer idUser, userAge;
    String userCity, userDescription, userName, userLastName1, userLastName2, userSocialNetwork,userState;
    Button contacto;
    TextView nombre,descripcion,age,city,redSocial;
    public OwnerContactFragment () {}
    private FragmentOwnerContactBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOwnerContactBinding.inflate(inflater, container, false);
        nombre = binding.getRoot().findViewById(R.id.nombre);
        descripcion = binding.getRoot().findViewById(R.id.descripcion);
        age = binding.getRoot().findViewById(R.id.age);
        city = binding.getRoot().findViewById(R.id.city);
        redSocial = binding.getRoot().findViewById(R.id.redSocial);
        Bundle args = getArguments();
        Integer petId = args.getInt("idPet");
        contacto = binding.getRoot().findViewById(R.id.button);
        idUser = args.getInt("idUser");

        infoUsuario();
        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("idPet", petId);
                bundle.putInt("idUser", idUser);
                EnviarRequestFragment requestFragment = new EnviarRequestFragment();
                requestFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.thirdFragment_, requestFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return binding.getRoot();
    }

    private void infoUsuario() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+idUser, null,
                response -> {
                    try {
                        userCity = response.getString("city");
                        userDescription = response.getString("description");
                        userAge = response.getInt("userAge");
                        userName = response.getString("name");
                        userLastName1 = response.getString("lastName1");
                        userLastName2 = response.getString("lastName2");
                        userSocialNetwork = response.getString("socialNetwork");
                        userState =  response.getString("state");
                        nombre.setText(userName+" "+userLastName1+" "+userLastName2);
                        descripcion.setText(userDescription);
                        age.setText(""+userAge);
                        city.setText(userCity);
                        redSocial.setText(userSocialNetwork);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(getContext()).add(request);
    }
}