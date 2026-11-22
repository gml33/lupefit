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
import com.lupefit.feature.configuracion.databinding.FragmentNotificacionesBinding;
import com.lupefit.feature.configuracion.viewmodel.NotificacionesViewModel;

public class NotificacionesFragment extends Fragment {

    private FragmentNotificacionesBinding binding;
    private NotificacionesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificacionesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new NotificacionesViewModel.Factory(requireContext())).get(NotificacionesViewModel.class);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        // Switches automatically update ViewModel via data binding
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}