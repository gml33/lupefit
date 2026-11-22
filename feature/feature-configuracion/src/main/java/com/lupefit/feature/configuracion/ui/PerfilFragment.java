package com.lupefit.feature.configuracion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.ui.R;
import com.lupefit.feature.configuracion.databinding.FragmentPerfilBinding;
import com.lupefit.feature.configuracion.viewmodel.PerfilConfigViewModel;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilConfigViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new PerfilConfigViewModel.Factory(requireContext())).get(PerfilConfigViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnGuardarPerfil.setOnClickListener(v -> viewModel.savePerfil());

        binding.etFechaNacimiento.setOnClickListener(v -> {
            // TODO: Show date picker
        });

        binding.etSexo.setOnClickListener(v -> {
            // TODO: Show sex selector dialog
        });

        binding.etNivelActividad.setOnClickListener(v -> {
            // TODO: Show activity level selector
        });

        binding.etObjetivo.setOnClickListener(v -> {
            // TODO: Show objective selector
        });
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Perfil guardado", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}