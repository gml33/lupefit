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
import com.lupefit.feature.configuracion.databinding.FragmentExportarBinding;
import com.lupefit.feature.configuracion.viewmodel.ExportarViewModel;

public class ExportarFragment extends Fragment {

    private FragmentExportarBinding binding;
    private ExportarViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExportarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new ExportarViewModel.Factory(requireContext())).get(ExportarViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnExportar.setOnClickListener(v -> viewModel.exportar());
    }

    private void setupObservers() {
        viewModel.archivoGenerado.observe(getViewLifecycleOwner(), archivo -> {
            if (archivo != null && !archivo.isEmpty()) {
                Toast.makeText(requireContext(), "Exportado: " + archivo, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}