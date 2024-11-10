package com.example.proyecto_ago_dic_2024;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonAdapter extends ArrayAdapter<Pokemon> {
    public PokemonAdapter(Context context, List<Pokemon> pokemons) {
        super(context, 0, pokemons);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pokemon pokemon = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pokemon_list_item, parent, false);
        }
        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        text1.setText(pokemon.getName());
        text2.setText("Height: " + pokemon.getHeight() + " Weight: " + pokemon.getWeight());
        Picasso.get().load(pokemon.getImageUrl()).into(imageView);
        return convertView;
    }
}
