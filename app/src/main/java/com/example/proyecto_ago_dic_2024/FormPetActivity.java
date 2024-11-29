package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormPetActivity extends AppCompatActivity implements OnMapReadyCallback {
    String seleccionado;
    EditText nombre, animal, raza, edad, genero, descripcion;
    ImageView imagen1, imagen2, imagen3;
    Bitmap bitmap1, bitmap2, bitmap3; // Para almacenar las imágenes seleccionadas
    Button btnEnviar;
    private GoogleMap mMap;
    private Marker userMarker; // Marca del usuario
    private double selectedLat = 0.0, selectedLng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pet);

        nombre = findViewById(R.id.nombreAnimal);
        animal = findViewById(R.id.animal);
        raza = findViewById(R.id.raza);
        edad = findViewById(R.id.edad);
        genero = findViewById(R.id.genero);
        descripcion = findViewById(R.id.descripcion);
        btnEnviar = findViewById(R.id.btnEnviar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        imagen1 = findViewById(R.id.petimageView1);
        imagen2 = findViewById(R.id.petimageView2);
        imagen3 = findViewById(R.id.petimageView3);
        Spinner spinner = findViewById(R.id.spinnerSeleccion);

        String[] opciones = {"Muy chico", "Chico", "Mediano", "Grande", "Muy grande"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionado = "Sin tamaño seleccionado";
            }
        });

        imagen1.setOnClickListener(v -> seleccionarImagen(1));
        imagen2.setOnClickListener(v -> seleccionarImagen(2));
        imagen3.setOnClickListener(v -> seleccionarImagen(3));

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDatos();
            }
        });


    }

    private void seleccionarImagen(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                if (requestCode == 1) {
                    bitmap1 = bitmap;
                    imagen1.setImageURI(uri);
                } else if (requestCode == 2) {
                    bitmap2 = bitmap;
                    imagen2.setImageURI(uri);
                } else if (requestCode == 3) {
                    bitmap3 = bitmap;
                    imagen3.setImageURI(uri);
                }

            } catch (IOException e) {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
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
        String url = "https://david255311.pythonanywhere.com/upload_pet";
        SharedPreferences sharedPreferences = getSharedPreferences("user_doc", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("user_id", "Valor por defecto");
        JSONObject jsonObject = new JSONObject();
        try {
            String uniqueID = UUID.randomUUID().toString();
            jsonObject.put("name", nombre.getText().toString());
            jsonObject.put("animal", animal.getText().toString());
            jsonObject.put("breed", raza.getText().toString());
            jsonObject.put("age", edad.getText().toString());
            jsonObject.put("gender", genero.getText().toString());
            jsonObject.put("description", descripcion.getText().toString());
            jsonObject.put("size", seleccionado);
            jsonObject.put("idUser", idUser);

            if (bitmap1 != null) {
                jsonObject.put("image1", convertirImagenABase64(bitmap1));
            }
            if (bitmap2 != null) {
                jsonObject.put("image2", convertirImagenABase64(bitmap2));
            }
            if (bitmap3 != null) {
                jsonObject.put("image3", convertirImagenABase64(bitmap3));
            }
            // Agrega las coordenadas solo si se seleccionó una ubicación
            if (selectedLat != 0.0 && selectedLng != 0.0) {
                jsonObject.put("latitude", selectedLat);
                jsonObject.put("longitude", selectedLng);
            } else {
                Toast.makeText(this, "Por favor selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
                return; // Detén la ejecución si no hay coordenadas
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Toast.makeText(this, "Mascota registrada exitosamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error al registrar la mascota: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        Log.d("ObjetoJSON", String.valueOf(jsonObject));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng initialLocation = new LatLng(19.432608, -99.133209); // Ciudad de México
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));

        mMap.setOnMapClickListener(latLng -> handleMapClick(latLng));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // Evento al iniciar el arrastre
                System.out.println("Iniciando arrastre del marcador...");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                System.out.println("Arrastrando marcador a: " + marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                System.out.println("Marcador movido a: " + marker.getPosition());
            }
        });
    }

    private void handleMapClick(LatLng latLng) {
        if (userMarker == null) {
            userMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Posición seleccionada")
                    .draggable(true));
        }
        selectedLat = userMarker.getPosition().latitude;
        selectedLng = userMarker.getPosition().longitude;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
