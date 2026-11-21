package com.lupefit.feature.ejercicio.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lupefit.feature.ejercicio.R;
import com.lupefit.feature.ejercicio.databinding.FragmentRutinaDetalleBinding;

public class RutinaDetalleFragment extends Fragment {

    private FragmentRutinaDetalleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRutinaDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Implementar detalle de rutina
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}