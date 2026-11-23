package com.lupefit.feature.perfil.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.feature.perfil.R;
import com.lupefit.feature.perfil.databinding.FragmentPerfilHomeBinding;

public class PerfilHomeFragment extends Fragment {

    private FragmentPerfilHomeBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        binding.optionEditarPerfil.setOnClickListener(v ->
                navController.navigate(R.id.action_perfilHomeFragment_to_editarPerfilFragment));
        binding.optionLogin.setOnClickListener(v ->
                navController.navigate(R.id.action_perfilHomeFragment_to_loginFragment));
        binding.optionRegistro.setOnClickListener(v ->
                navController.navigate(R.id.action_perfilHomeFragment_to_registroFragment));
        binding.optionSyncAccount.setOnClickListener(v ->
                navController.navigate(R.id.action_perfilHomeFragment_to_syncAccountFragment));
        binding.optionCambiarPassword.setOnClickListener(v ->
                navController.navigate(R.id.action_perfilHomeFragment_to_cambiarPasswordFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
