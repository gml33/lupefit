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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.core.ui.R;
import com.lupefit.feature.configuracion.databinding.FragmentPresupuestoBinding;
import com.lupefit.feature.configuracion.viewmodel.PresupuestoViewModel;

public class PresupuestoFragment extends Fragment {

    private FragmentPresupuestoBinding binding;
    private PresupuestoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPresupuestoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new PresupuestoViewModel.Factory(requireContext())).get(PresupuestoViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupRecyclerView() {
        binding.rvHistorialPresupuesto.setLayoutManager(new LinearLayoutManager(requireContext()));
        // TODO: Set adapter
    }

    private void setupClickListeners() {
        binding.btnVerItemsSinPrecio.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}