package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

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
        getMascotas();
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
        addPointsToMap();
    }

    private void addPointsToMap () {
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(19.432608,-99.133209));
        points.add(new LatLng(20.659698,-103.349609));
        points.add(new LatLng(25.686614,-100.316113));
        points.add(new LatLng(21.161908,-86.851528));
        points.add(new LatLng(19.4978,-99.1269));

        for(LatLng point : points){
            mMap.addMarker(new MarkerOptions().position(point).title("Punto de interés"));
        }

        LatLng firstPoint = points.get(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPoint,5));
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
        openPetInfoFragment(marker);
    }

    private void openPetInfoFragment(Marker marker) {
        Bundle bundle = new Bundle();
        bundle.putString("pet_id", marker.getTitle().toString());

        PetInfoFragment petInfoFragment = new PetInfoFragment();
        petInfoFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.thirdFragment_, petInfoFragment)
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
                            int idUser  = pet.getInt("idUser");
                            int idPet =pet.getInt("idPet");
                            String name = pet.getString("name");
                            String animal = pet.getString("animal");
                            String breed = pet.getString("breed");
                            Integer age = pet.getInt("age");
                            Integer gender = pet.getInt("gender");
                            String size = pet.getString("size");
                            String description = pet.getString("description");
                            String image1 = null;
                            String image2 = null;
                            String image3 = null;
                            if(!pet.getString("image1").isEmpty()){
                                image1 = pet.getString("image1");
                            }

                            if(!pet.getString("image2").isEmpty()){
                                image2 = pet.getString("image2");
                            }

                            if(!pet.getString("image3").isEmpty()){
                                image3 = pet.getString("image3");
                            }

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

                            Pet petO = new Pet(idPet, idUser , name, animal, breed, age, gender, size, description, image1, image2, image3, lat, lon);
                            petsA.add(petO);

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