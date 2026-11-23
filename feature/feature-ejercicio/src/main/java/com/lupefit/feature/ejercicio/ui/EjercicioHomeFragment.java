package com.lupefit.feature.ejercicio.ui;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.feature.ejercicio.R;
import com.lupefit.feature.ejercicio.databinding.FragmentEjercicioHomeBinding;
import com.lupefit.feature.ejercicio.viewmodel.EjercicioHomeViewModel;

public class EjercicioHomeFragment extends Fragment {

    private FragmentEjercicioHomeBinding binding;
    private EjercicioHomeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEjercicioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(EjercicioHomeViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        // Quick action cards
        binding.cardBuscarEjercicio.setOnClickListener(v -> {
            navController.navigate(R.id.action_ejercicioHomeFragment_to_buscarEjercicioFragment);
        });

        binding.cardNuevaRutina.setOnClickListener(v -> {
            Bundle arguments = new Bundle();
            arguments.putBoolean("create", true);
            navController.navigate(R.id.action_ejercicioHomeFragment_to_rutinaDetalleFragment, arguments);
        });

        binding.cardWodTimer.setOnClickListener(v -> showUnavailable());

        binding.cardMisRutinas.setOnClickListener(v -> {
            navController.navigate(R.id.action_ejercicioHomeFragment_to_rutinaDetalleFragment);
        });
    }

    private void showUnavailable() {
        Toast.makeText(requireContext(), com.lupefit.core.ui.R.string.funcion_no_disponible,
                Toast.LENGTH_SHORT).show();
    }

    private void setupObservers() {
        // TODO: Observe recent workouts LiveData from ViewModel
        // For now, show empty state
        binding.emptyState.getRoot().setVisibility(View.VISIBLE);
        binding.rvRecentWorkouts.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}