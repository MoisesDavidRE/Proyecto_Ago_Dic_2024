package com.example.proyecto_ago_dic_2024;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RequestDetailFragment extends Fragment {
    TextView mensaje;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);
        Bundle args = getArguments();

        mensaje = view.findViewById(R.id.mensaje);
        mensaje.setText(args.getString("message"));

        return view;
    }
}