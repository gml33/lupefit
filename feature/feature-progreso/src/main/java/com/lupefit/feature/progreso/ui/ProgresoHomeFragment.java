package com.lupefit.feature.progreso.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        binding.cardRegistrarMedidas.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToMedidasFragment()));

        // Card: Tomar Foto
        binding.cardTomarFoto.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToFotosFragment()));

        // Card: Ver Gráficos
        binding.cardVerGraficos.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToGraficosProgresoFragment()));

        // Button: Exportar
        binding.btnExportar.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToExportarFragment()));

        // Button: Ver Gráficos (bottom)
        binding.btnVerGraficos.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToGraficosProgresoFragment()));

        // Empty state button
        binding.emptyState.btnFirstWeight.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToPesoFragment()));
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
