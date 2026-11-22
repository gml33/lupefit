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
import com.lupefit.feature.configuracion.databinding.FragmentCuentaBinding;
import com.lupefit.feature.configuracion.viewmodel.CuentaViewModel;

public class CuentaFragment extends Fragment {

    private FragmentCuentaBinding binding;
    private CuentaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCuentaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new CuentaViewModel.Factory(requireContext())).get(CuentaViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnGuardarCuenta.setOnClickListener(v -> viewModel.saveCambios());
        binding.btnCambiarPassword.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show());
        binding.btnAutenticacionDosFactores.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show());
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
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