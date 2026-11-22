package com.lupefit.feature.perfil.ui;

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
import com.lupefit.feature.perfil.databinding.FragmentSyncAccountBinding;
import com.lupefit.feature.perfil.viewmodel.SyncAccountViewModel;

public class SyncAccountFragment extends Fragment {

    private FragmentSyncAccountBinding binding;
    private SyncAccountViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSyncAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new SyncAccountViewModel.Factory(requireContext())).get(SyncAccountViewModel.class);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnVincular.setOnClickListener(v -> {
            // TODO: Show login dialog for server account
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show();
        });

        binding.btnDesvincular.setOnClickListener(v -> viewModel.desvincularCuenta());

        binding.btnSincronizarAhora.setOnClickListener(v -> viewModel.sincronizarAhora());

        binding.switchSyncAuto.setOnClickListener(v -> viewModel.toggleSyncAutomatica());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}