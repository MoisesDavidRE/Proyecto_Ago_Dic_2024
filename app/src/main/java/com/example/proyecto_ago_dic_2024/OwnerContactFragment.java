package com.example.proyecto_ago_dic_2024;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.proyecto_ago_dic_2024.databinding.FragmentOwnerContactBinding;

public class OwnerContactFragment extends Fragment {

    public OwnerContactFragment () {}
    private FragmentOwnerContactBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOwnerContactBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}