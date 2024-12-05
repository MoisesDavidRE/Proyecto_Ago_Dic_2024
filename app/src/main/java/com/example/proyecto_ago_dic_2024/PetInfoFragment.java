package com.example.proyecto_ago_dic_2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PetInfoFragment extends Fragment {
    private Button buttonRequest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_info, container, false);
        Bundle args = getArguments();
        Integer petId = args.getInt("idPet");
        Integer idUser = args.getInt("idUser");
        String name = args.getString("name");
        String animal = args.getString("animal");
        String breed = args.getString("breed");
        Integer age = args.getInt("age");
        String gender = args.getString("gender");
        String description = args.getString("description");
        String size = args.getString("size");
        String imageUrl = args.getString("image1");
        String image2 = args.getString("image2");
        String image3 = args.getString("image3");

        Button buttonRequest = view.findViewById(R.id.btnContactRequest);

        TextView petAnimal = view.findViewById(R.id.tvAnimal);
        TextView petDescription = view.findViewById(R.id.tvDescription);
        TextView petName = view.findViewById(R.id.tvPetName);
        ImageView image1 = view.findViewById(R.id.ivPetImage);
        TextView petGender = view.findViewById(R.id.tvGender);
        TextView petAge = view.findViewById(R.id.tvAge);
        petAge.setText("Años: "+ age);

        petName.setText(name);
        petDescription.setText(description);
        petAnimal.setText(animal+": "+ breed);
        petGender.setText(gender);
        Picasso.get().load(imageUrl).into(image1);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("idPet", petId);
                bundle.putInt("idUser", idUser);
                OwnerContactFragment ownerContactFragment = new OwnerContactFragment();
                ownerContactFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.thirdFragment_, ownerContactFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}