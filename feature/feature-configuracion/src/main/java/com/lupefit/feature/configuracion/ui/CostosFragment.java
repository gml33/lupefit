package com.lupefit.feature.configuracion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.ui.R;
import com.lupefit.feature.configuracion.databinding.FragmentCostosBinding;
import com.lupefit.feature.configuracion.viewmodel.CostosViewModel;

public class CostosFragment extends Fragment {

    private FragmentCostosBinding binding;
    private CostosViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCostosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new CostosViewModel.Factory(requireContext())).get(CostosViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.switchCostos.setOnCheckedChangeListener((button, checked) -> viewModel.setEnabled(checked));
        binding.etMoneda.setOnFocusChangeListener((input, focused) -> {
            if (!focused) {
                String currency = binding.etMoneda.getText().toString().trim().toUpperCase(java.util.Locale.ROOT);
                if (currency.matches("[A-Z]{3}")) {
                    viewModel.setMoneda(currency);
                }
            }
        });
        binding.etDiasAlerta.setOnFocusChangeListener((input, focused) -> {
            if (!focused) {
                String days = binding.etDiasAlerta.getText().toString().trim();
                if (days.matches("[0-9]{1,3}")) {
                    viewModel.setDiasAlerta(Integer.parseInt(days));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}