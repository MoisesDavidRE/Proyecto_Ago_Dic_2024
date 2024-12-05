package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private ListView listView;
    private PetAdapter adapter;
    private ArrayList<Pet> petList;
    private RequestQueue requestQueue;
    Button btnAgregarMascota;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        listView = view.findViewById(R.id.listViewPokemon);
        petList = new ArrayList<>();
        adapter = new PetAdapter(getActivity(), petList);
        btnAgregarMascota = view.findViewById(R.id.button2);

        btnAgregarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FormPetActivity.class);
                startActivity(intent);
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        fetchPetsData();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet selectedPet = petList.get(position);
                NavController navController = NavHostFragment.findNavController(getParentFragment());
                Bundle bundle = new Bundle();
                bundle.putInt("idPet", selectedPet.getIdPet());
                bundle.putString("name", selectedPet.getName());
                bundle.putString("animal", selectedPet.getAnimal());
                bundle.putString("breed", selectedPet.getBreed());
                bundle.putInt("age", selectedPet.getAge());
                bundle.putInt("gender", selectedPet.getGender());
                bundle.putString("size", selectedPet.getSize());
                bundle.putString("description", selectedPet.getDescription());
                bundle.putString("image1", selectedPet.getImage1());
                bundle.putString("image2", selectedPet.getImage2());
                bundle.putString("image3", selectedPet.getImage3());
                bundle.putDouble("lat", selectedPet.getLat());
                bundle.putDouble("lon", selectedPet.getLon());

                navController.navigate(R.id.petDetailFragment, bundle);
            }
        });
        return view;
    }

    private void fetchPetsData() {
        String url = "https://david255311.pythonanywhere.com/userPets/";
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_doc", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("user_id", "Valor por defecto");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+idUser, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("pets");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject pet = results.getJSONObject(i);
                                Integer idUser  = pet.getInt("idUser");
                                Integer idPet = pet.getInt("idPet");
                                String name = pet.getString("name");
                                String animal = pet.getString("animal");
                                String breed = pet.getString("breed");
                                Integer age = pet.getInt("age");
                                Integer gender = pet.getInt("gender");
                                String size = pet.getString("size");
                                String description = pet.getString("description");
                                String image1 = pet.getString("image1");
                                String image2 = pet.getString("image2");
                                String image3 = pet.getString("image3");

                                double lat = pet.getDouble("lat");
                                double lon = pet.getDouble("lon");
                                Boolean req;
                                if(pet.getInt("request") == 1){
                                    req = true;
                                } else { req = false; }

                                Pet petO = new Pet(idPet, idUser , name, animal, breed, age, gender, size,
                                        description, image1, image2, image3, lat, lon, req);
                                petList.add(petO);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}
