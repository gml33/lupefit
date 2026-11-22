package com.lupefit.feature.agua.ui;

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
import com.lupefit.feature.agua.databinding.FragmentConfigurarMetaBinding;
import com.lupefit.feature.agua.viewmodel.ConfigurarMetaViewModel;

public class ConfigurarMetaFragment extends Fragment {

    private FragmentConfigurarMetaBinding binding;
    private ConfigurarMetaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfigurarMetaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new ConfigurarMetaViewModel.Factory(requireContext())).get(ConfigurarMetaViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        // Guardar meta
        binding.btnGuardarMeta.setOnClickListener(v -> viewModel.saveMeta());

        // Presets
        binding.btnPreset2000.setOnClickListener(v -> viewModel.setPreset(2000));
        binding.btnPreset2500.setOnClickListener(v -> viewModel.setPreset(2500));
        binding.btnPreset3000.setOnClickListener(v -> viewModel.setPreset(3000));

        // Text watcher for meta input
        binding.etMeta.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                viewModel.updateMetaInput(s.toString());
            }
        });
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Meta guardada", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.currentGoalStr.observe(getViewLifecycleOwner(), goal -> {
            if (goal != null) {
                binding.tvCurrentGoal.setText(goal);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}