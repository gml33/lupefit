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
import com.lupefit.feature.configuracion.databinding.FragmentSyncBinding;
import com.lupefit.feature.configuracion.viewmodel.SyncViewModel;

public class SyncFragment extends Fragment {

    private FragmentSyncBinding binding;
    private SyncViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSyncBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new SyncViewModel.Factory(requireContext())).get(SyncViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnSincronizarAhora.setOnClickListener(v -> viewModel.sincronizarAhora());
        binding.switchSyncAuto.setOnClickListener(v -> viewModel.toggleSyncAuto());
        binding.switchSoloWifi.setOnClickListener(v -> viewModel.toggleSoloWifi());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}