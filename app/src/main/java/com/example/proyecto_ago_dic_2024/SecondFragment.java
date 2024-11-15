package com.example.proyecto_ago_dic_2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemonList;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        listView = view.findViewById(R.id.listViewPokemon);
        pokemonList = new ArrayList<>();
        adapter = new PokemonAdapter(getActivity(), pokemonList);
        listView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getActivity());
        fetchPokemonData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pokemon selectedPokemon = pokemonList.get(position);
                Intent intent = new Intent(getActivity(), PokemonDetailActivity.class);
                intent.putExtra("name", selectedPokemon.getName());
                intent.putExtra("height", ""+selectedPokemon.getHeight());
                intent.putExtra("weight", ""+selectedPokemon.getWeight());
                intent.putExtra("image", selectedPokemon.getImageUrl());
                startActivity(intent);
            }
        });
        return view;
    }

    private void fetchPokemonData() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=15";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject pokemonObject = results.getJSONObject(i);
                                String pokemonUrl = pokemonObject.getString("url");
                                fetchPokemonDetails(pokemonUrl);
                            }
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

    private void fetchPokemonDetails(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            int weight = response.getInt("weight");
                            int height = response.getInt("height");
                            String urlImage = response.getJSONObject("sprites").getString("front_default");

                            Pokemon pokemon = new Pokemon(name, urlImage, weight, height);
                            pokemonList.add(pokemon);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Cargando...", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}
