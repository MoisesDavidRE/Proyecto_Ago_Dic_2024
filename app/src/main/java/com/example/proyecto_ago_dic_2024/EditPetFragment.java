package com.example.proyecto_ago_dic_2024;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditPetFragment extends Fragment {
    EditText name, animal, breed, age, gender, description;
    ImageView image1, image2, image3;
    Spinner spinner;
    String size;
    Integer idPet;
    double lat, lon;
    Bitmap bitmap1, bitmap2, bitmap3; // Para almacenar imágenes seleccionadas
    Button btnSave;
    GoogleMap mMap;
    Marker userMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_pet, container, false);

        Bundle args = getArguments();

        name = view.findViewById(R.id.nombreAnimal);
        animal = view.findViewById(R.id.animal);
        breed = view.findViewById(R.id.raza);
        age = view.findViewById(R.id.edad);
        gender = view.findViewById(R.id.genero);
        description = view.findViewById(R.id.descripcion);
        image1 = view.findViewById(R.id.petimageView1);
        image2 = view.findViewById(R.id.petimageView2);
        image3 = view.findViewById(R.id.petimageView3);
        btnSave = view.findViewById(R.id.btnEnviar);

        // Asignación de valores de argumentos
        name.setText(args.getString("name"));
        animal.setText(args.getString("animal"));
        breed.setText(args.getString("breed"));
        age.setText(String.valueOf(args.getInt("age")));
        gender.setText(String.valueOf(args.getInt("gender")));
        description.setText(args.getString("description"));
        idPet = args.getInt("idPet");

        lat = args.getDouble("lat");
        lon = args.getDouble("lon");

        // Configuración del Spinner
        spinner = view.findViewById(R.id.spinnerSeleccion);
        String[] opciones = {"Muy chico", "Chico", "Mediano", "Grande", "Muy grande"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        size = args.getString("size", "Mediano");
        int index = java.util.Arrays.asList(opciones).indexOf(size);
        if (index >= 0) {
            spinner.setSelection(index);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                size = "Sin tamaño seleccionado";
            }
        });

        // Carga de imágenes con Picasso
        Picasso.get().load(args.getString("image1", "")).into(image1);
        Picasso.get().load(args.getString("image2", "")).into(image2);
        Picasso.get().load(args.getString("image3", "")).into(image3);

        image1.setOnClickListener(v -> seleccionarImagen(1));
        image2.setOnClickListener(v -> seleccionarImagen(2));
        image3.setOnClickListener(v -> seleccionarImagen(3));

        // Configuración del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                LatLng location = new LatLng(lat, lon);
                userMarker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title("Ubicación de la mascota")
                        .draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        lat = marker.getPosition().latitude;
                        lon = marker.getPosition().longitude;
                    }
                });
            });
        }

        btnSave.setOnClickListener(v -> enviarDatos());

        return view;
    }

    private void seleccionarImagen(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);

                if (requestCode == 1) {
                    bitmap1 = bitmap;
                    image1.setImageBitmap(bitmap);
                } else if (requestCode == 2) {
                    bitmap2 = bitmap;
                    image2.setImageBitmap(bitmap);
                } else if (requestCode == 3) {
                    bitmap3 = bitmap;
                    image3.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertirImagenABase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void enviarDatos() {
        String url = "https://david255311.pythonanywhere.com/update_pet";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idPet", idPet);
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("animal", animal.getText().toString());
            jsonObject.put("breed", breed.getText().toString());
            jsonObject.put("age", age.getText().toString());
            jsonObject.put("gender", gender.getText().toString());
            jsonObject.put("description", description.getText().toString());
            jsonObject.put("size", size);
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lon);

            if (bitmap1 != null) jsonObject.put("image1", convertirImagenABase64(bitmap1));
            if (bitmap2 != null) jsonObject.put("image2", convertirImagenABase64(bitmap2));
            if (bitmap3 != null) jsonObject.put("image3", convertirImagenABase64(bitmap3));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                response -> Toast.makeText(getContext(), "Datos actualizados exitosamente", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(getContext(), "Error al actualizar: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}
