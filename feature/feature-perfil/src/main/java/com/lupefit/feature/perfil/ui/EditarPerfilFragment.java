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
import com.lupefit.feature.perfil.databinding.FragmentEditarPerfilBinding;
import com.lupefit.feature.perfil.viewmodel.EditarPerfilViewModel;

public class EditarPerfilFragment extends Fragment {

    private FragmentEditarPerfilBinding binding;
    private EditarPerfilViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditarPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new EditarPerfilViewModel.Factory(requireContext())).get(EditarPerfilViewModel.class);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnGuardarPerfil.setOnClickListener(v -> viewModel.guardarPerfil());

        binding.etFechaNacimiento.setOnClickListener(v -> {
            // TODO: Show date picker
        });

        binding.etSexo.setOnClickListener(v -> {
            // TODO: Show sex selector
        });

        binding.etNivelActividad.setOnClickListener(v -> {
            // TODO: Show activity level selector
        });

        binding.etObjetivo.setOnClickListener(v -> {
            // TODO: Show objective selector
        });
    }

    private void setupObservers() {
        viewModel.guardado.observe(getViewLifecycleOwner(), guardado -> {
            if (guardado != null && guardado) {
                Toast.makeText(requireContext(), "Perfil guardado", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
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