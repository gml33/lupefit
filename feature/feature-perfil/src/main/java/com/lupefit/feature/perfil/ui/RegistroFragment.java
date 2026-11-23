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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.core.ui.R;
import com.lupefit.feature.perfil.databinding.FragmentRegistroBinding;
import com.lupefit.feature.perfil.viewmodel.RegistroViewModel;

public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private RegistroViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new RegistroViewModel.Factory(requireContext())).get(RegistroViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.btnRegistro.setOnClickListener(v -> {
            viewModel.setNombre(binding.etNombre.getText().toString());
            viewModel.setApellido(binding.etApellido.getText().toString());
            viewModel.setEmail(binding.etEmail.getText().toString());
            viewModel.setPassword(binding.etPassword.getText().toString());
            viewModel.setConfirmPassword(binding.etConfirmPassword.getText().toString());
            viewModel.setTerminos(binding.cbTerminos.isChecked());
            viewModel.registrar();
        });

        binding.btnIrLogin.setOnClickListener(v ->
                navController.navigate(RegistroFragmentDirections.Companion.actionRegistroFragmentToLoginFragment()));
    }

    private void setupObservers() {
        viewModel.registrado.observe(getViewLifecycleOwner(), registrado -> {
            if (registrado != null && registrado) {
                Toast.makeText(requireContext(), "Cuenta creada", Toast.LENGTH_SHORT).show();
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