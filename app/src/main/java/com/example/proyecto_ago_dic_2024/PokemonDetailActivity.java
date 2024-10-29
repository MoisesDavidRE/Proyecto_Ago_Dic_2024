package com.example.proyecto_ago_dic_2024;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class PokemonDetailActivity extends AppCompatActivity {

    TextView nombrePokemon;
    TextView pesoPokemon;
    TextView alturaPokemon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);
        nombrePokemon = (TextView) findViewById(R.id.NombreDetalle);
        pesoPokemon = (TextView) findViewById(R.id.Peso);
        alturaPokemon = (TextView) findViewById(R.id.Altura);
        ImageView imageView = findViewById(R.id.imagePokemon);
        Bundle bundle = getIntent().getExtras();
        nombrePokemon.setText(bundle.getString("name").toUpperCase());
        pesoPokemon.setText("Peso: "+bundle.getString("weight"));
        alturaPokemon.setText("Altura: "+ bundle.getString("height"));
        Picasso.get().load(bundle.getString("image")).into(imageView);
    }
}