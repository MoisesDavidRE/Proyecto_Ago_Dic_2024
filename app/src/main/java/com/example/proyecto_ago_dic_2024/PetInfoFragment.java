package com.example.proyecto_ago_dic_2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PetInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_info, container, false);
        String petId = getArguments().getString("pet_id");
        TextView petInfoTextView = view.findViewById(R.id.pet_info_text);
        petInfoTextView.setText("Detalles de la mascota con ID: " + petId);

        return view;
    }
}