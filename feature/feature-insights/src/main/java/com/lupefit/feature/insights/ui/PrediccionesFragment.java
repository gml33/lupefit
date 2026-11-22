package com.lupefit.feature.insights.ui;

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
import com.lupefit.feature.insights.databinding.FragmentPrediccionesBinding;
import com.lupefit.feature.insights.viewmodel.PrediccionesViewModel;

public class PrediccionesFragment extends Fragment {

    private FragmentPrediccionesBinding binding;
    private PrediccionesViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPrediccionesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new PrediccionesViewModel.Factory(requireContext())).get(PrediccionesViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setTitle(getString(R.string.predicciones));
    }

    private void setupClickListeners() {
        binding.btnRecargar.setOnClickListener(v -> viewModel.recargar());
    }

    private void setupObservers() {
        viewModel.cargando.observe(getViewLifecycleOwner(), cargando -> {
            binding.progressBar.setVisibility(cargando != null && cargando ? View.VISIBLE : View.GONE);
            binding.btnRecargar.setEnabled(cargando == null || !cargando);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}