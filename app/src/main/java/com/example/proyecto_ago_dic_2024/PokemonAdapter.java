package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonAdapter extends ArrayAdapter<Pet> {
    public PokemonAdapter(Context context, List<Pet> pets) {
        super(context, 0, pets);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener el objeto mascota actual
        Pet pet = getItem(position);

        // Inflar el layout si es necesario
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pokemon_list_item, parent, false);
        }

        // Referencias a los elementos del layout
        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        ImageView notificationIcon = convertView.findViewById(R.id.notificationView);

        // Configurar los textos
        text1.setText(pet.getName());
        text2.setText("Animal: " + pet.getAnimal() + " Raza: " + pet.getBreed());

        // Cargar la imagen principal con Picasso
        Picasso.get().load(pet.getImage1()).into(imageView);

        // Asegurarse de configurar correctamente la visibilidad de notificationIcon
        if (pet.hasPendingRequests()) {
            notificationIcon.setVisibility(View.VISIBLE); // Mostrar la campana
        } else {
            notificationIcon.setVisibility(View.GONE); // Ocultarla si no hay solicitudes
        }

        // Log para depurar datos
        Log.d("Datos", "Mascota: " + pet.getName() + ", Tiene solicitudes pendientes: " + pet.hasPendingRequests());

        return convertView;
    }

}