package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import android.widget.Toast;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThirdFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    int idUser;
    int idPet;
    String name;
    String animal;
    String breed;
    Integer age;
    Integer gender;
    String size;
    String description;
    String image1;
    String image2;
    String image3;
    private final HashMap<Marker, Pet> markerPetMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    public ThirdFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.thirdFragment_);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_doc", Context.MODE_PRIVATE);
                    String nombre = sharedPreferences.getString("user_name","Valor por defecto");
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Dueño:"+nombre).snippet("Clic para ver más"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
                }
            }
        });
        googleMap.setOnInfoWindowClickListener(this);
        getMascotas();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                onMapReady(mMap);
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Pet selectedPet = markerPetMap.get(marker); // Obtén la mascota asociada al marcador

        if (selectedPet != null) {
            openPetInfoFragment(selectedPet); // Envía la mascota al nuevo fragmento
        } else {
            Toast.makeText(getContext(), "No se encontró información de la mascota.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPetInfoFragment(Pet pet) {
        Bundle bundle = new Bundle();
        bundle.putInt("idPet", pet.getIdPet());
        bundle.putInt("idUser", pet.getIdUser());
        bundle.putString("name", pet.getName());
        bundle.putString("animal", pet.getAnimal());
        bundle.putString("breed", pet.getBreed());
        bundle.putInt("age", pet.getAge());
        bundle.putInt("gender", pet.getGender());
        bundle.putString("size", pet.getSize());
        bundle.putString("description", pet.getDescription());
        bundle.putString("image1", pet.getImage1());
        bundle.putString("image2", pet.getImage2());
        bundle.putString("image3", pet.getImage3());
        bundle.putDouble("lat", pet.getLat());
        bundle.putDouble("lon", pet.getLon());

        PetInfoFragment petInfoFragment = new PetInfoFragment();
        petInfoFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.thirdFragment_, petInfoFragment)
                .addToBackStack(null) // Permitir regresar al mapa
                .commit();
    }


    private void getMascotas() {
        String apiUrl = "https://david255311.pythonanywhere.com/pets";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        JSONArray pets = response.getJSONArray("pets");
                        List<Pet> petsA = new ArrayList<>();

                        for (int i = 0; i < pets.length(); i++) {
                            JSONObject pet = pets.getJSONObject(i);
                             idUser  = pet.getInt("idUser");
                             idPet = pet.getInt("idPet");
                             name = pet.getString("name");
                             animal = pet.getString("animal");
                             breed = pet.getString("breed");
                             age = pet.getInt("age");
                             gender = pet.getInt("gender");
                             size = pet.getString("size");
                             description = pet.getString("description");
                             image1 = pet.getString("image1");
                             image2 = pet.getString("image2");
                             image3 = pet.getString("image3");

                            double lat = pet.getDouble("lat");
                            double lon = pet.getDouble("lon");

                            JSONObject snippetData = new JSONObject();
                            snippetData.put("name", name);
                            snippetData.put("gender", gender);
                            snippetData.put("imageUrl", image1);

                            LatLng petLocation = new LatLng(lat, lon);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(petLocation)
                                    .title(name)
                                    .snippet(snippetData.toString());
                            mMap.addMarker(markerOptions);

                            Pet petO = new Pet(idPet, idUser , name, animal, breed, age, gender, size,
                                               description, image1, image2, image3, lat, lon);
                            petsA.add(petO);
                            Marker marker = mMap.addMarker(markerOptions);
                            markerPetMap.put(marker, petO); // Asocia el marcador con el objeto Pet


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error al obtener eventos", Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(getContext()).add(request);
    }

}