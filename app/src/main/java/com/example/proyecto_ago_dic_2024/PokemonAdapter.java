package com.example.proyecto_ago_dic_2024;

import android.content.Context;
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
        Pet pet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pokemon_list_item, parent, false);
        }
        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        text1.setText(pet.getName());
        text2.setText("Animal: " + pet.getAnimal() + "Raza: " + pet.getBreed());
        Log.d("Datos", pet.getName()+pet.getBreed()+pet.getAnimal());
        Picasso.get().load(pet.getImage1()).into(imageView);
        return convertView;
    }
}