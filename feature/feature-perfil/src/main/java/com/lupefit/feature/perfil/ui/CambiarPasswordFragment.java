package com.lupefit.feature.perfil.ui;

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
import com.lupefit.feature.perfil.databinding.FragmentCambiarPasswordBinding;
import com.lupefit.feature.perfil.viewmodel.CambiarPasswordViewModel;

public class CambiarPasswordFragment extends Fragment {

    private FragmentCambiarPasswordBinding binding;
    private CambiarPasswordViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new CambiarPasswordViewModel.Factory(requireContext())).get(CambiarPasswordViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnCambiarPassword.setOnClickListener(v -> viewModel.cambiarPassword());
    }

    private void setupObservers() {
        viewModel.cambiado.observe(getViewLifecycleOwner(), cambiado -> {
            if (cambiado != null && cambiado) {
                Toast.makeText(requireContext(), "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
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