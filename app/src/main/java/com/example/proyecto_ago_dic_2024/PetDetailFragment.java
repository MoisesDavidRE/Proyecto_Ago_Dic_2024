package com.example.proyecto_ago_dic_2024;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class PetDetailFragment extends Fragment {

    private TextView nombre, animal, raza, edad, genero, tamanio, descripcion;
    private ImageView imageView;
    private Button btnEditar, btnEliminar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_detail, container, false);

        // Vincular las vistas del diseño
        nombre = view.findViewById(R.id.NombreDetalle);
        animal = view.findViewById(R.id.Animal);
        raza = view.findViewById(R.id.Raza);
        edad = view.findViewById(R.id.Edad);
        genero = view.findViewById(R.id.Genero);
        tamanio = view.findViewById(R.id.Tamanio);
        descripcion = view.findViewById(R.id.Descripcion);
        imageView = view.findViewById(R.id.imagePet);
        btnEditar = view.findViewById(R.id.btnEdit);
        btnEliminar = view.findViewById(R.id.btnDelete);
        btnEliminar.setOnClickListener(v -> showDeleteConfirmationDialog(getArguments().getInt("idPet")));

        if (getArguments() != null) {
            nombre.setText(getArguments().getString("name", "").toUpperCase());
            animal.setText("Animal: " + getArguments().getString("animal", ""));
            raza.setText("Raza: " + getArguments().getString("breed", ""));
            edad.setText("Edad: " + getArguments().getInt("age", 0));
            genero.setText("Género: " + getArguments().getInt("gender", 0));
            tamanio.setText("Tamaño: " + getArguments().getString("size", ""));
            descripcion.setText("Descripción: " + getArguments().getString("description", ""));
            Picasso.get().load(getArguments().getString("image1", "")).into(imageView);
        }

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = NavHostFragment.findNavController(getParentFragment());
                Bundle bundle = new Bundle();

                bundle.putInt("idPet", getArguments().getInt("idPet"));
                bundle.putString("name", getArguments().getString("name"));
                bundle.putString("animal", getArguments().getString("animal"));
                bundle.putString("breed", getArguments().getString("breed"));
                bundle.putInt("age", getArguments().getInt("age"));
                bundle.putInt("gender", getArguments().getInt("gender"));
                bundle.putString("size", getArguments().getString("size"));
                bundle.putString("description", getArguments().getString("description"));
                bundle.putString("image1", getArguments().getString("image1"));
                bundle.putString("image2", getArguments().getString("image2"));
                bundle.putString("image3", getArguments().getString("image3"));
                bundle.putDouble("lat", getArguments().getDouble("lat"));
                bundle.putDouble("lon", getArguments().getDouble("lon"));
                navController.navigate(R.id.editPetFragment, bundle);
            }
        });

        return view;
    }

    private void showDeleteConfirmationDialog(Integer idPet) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta mascota? Esta acción es irreversible")
                .setPositiveButton("Eliminar", (dialog, which) -> deletePet(idPet))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deletePet(Integer idPet) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://david255311.pythonanywhere.com/delete_pet/" + idPet;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    Toast.makeText(getContext(), "Mascota eliminada", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getContext(), "Error al eliminar mascota", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(request);
    }
}
