package com.lupefit.feature.progreso.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.core.ui.R;
import com.lupefit.feature.progreso.databinding.FragmentProgresoHomeBinding;
import com.lupefit.feature.progreso.viewmodel.ProgresoHomeViewModel;

public class ProgresoHomeFragment extends Fragment {

    private FragmentProgresoHomeBinding binding;
    private ProgresoHomeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgresoHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(ProgresoHomeViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        // Card: Registrar Peso
        binding.cardRegistrarPeso.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToPesoFragment()));

        // Card: Registrar Medidas
        binding.cardRegistrarMedidas.setOnClickListener(v -> showUnavailable());

        // Card: Tomar Foto
        binding.cardTomarFoto.setOnClickListener(v -> showUnavailable());

        // Card: Ver Gráficos
        binding.cardVerGraficos.setOnClickListener(v -> showUnavailable());

        // Button: Exportar
        binding.btnExportar.setOnClickListener(v -> showUnavailable());

        // Button: Ver Gráficos (bottom)
        binding.btnVerGraficos.setOnClickListener(v -> showUnavailable());

        // Empty state button
        binding.emptyState.btnFirstWeight.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToPesoFragment()));
    }

    private void showUnavailable() {
        Toast.makeText(requireContext(), com.lupefit.core.ui.R.string.funcion_no_disponible,
                Toast.LENGTH_SHORT).show();
    }

    private void setupObservers() {
        // TODO: Observe ViewModel LiveData for current weight, initial weight, diff
        // viewModel.currentWeight.observe(getViewLifecycleOwner(), weight -> ...)
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
