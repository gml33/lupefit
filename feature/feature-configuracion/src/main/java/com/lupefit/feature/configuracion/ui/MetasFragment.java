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
import com.lupefit.feature.configuracion.databinding.FragmentMetasBinding;
import com.lupefit.feature.configuracion.viewmodel.MetasConfigViewModel;

public class MetasFragment extends Fragment {

    private FragmentMetasBinding binding;
    private MetasConfigViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMetasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new MetasConfigViewModel.Factory(requireContext())).get(MetasConfigViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnGuardarMetas.setOnClickListener(v -> viewModel.saveMetas());

        binding.btnPresetPerderPeso.setOnClickListener(v -> viewModel.setPreset(1800, 140, 180, 55));
        binding.btnPresetMantener.setOnClickListener(v -> viewModel.setPreset(2200, 160, 220, 65));
        binding.btnPresetGanarMasa.setOnClickListener(v -> viewModel.setPreset(2800, 200, 300, 80));

        binding.switchMetasPorComida.setOnClickListener(v -> viewModel.toggleMetasPorComida());
        binding.switchNetCarbs.setOnClickListener(v -> viewModel.toggleNetCarbs());
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Metas guardadas", Toast.LENGTH_SHORT).show();
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