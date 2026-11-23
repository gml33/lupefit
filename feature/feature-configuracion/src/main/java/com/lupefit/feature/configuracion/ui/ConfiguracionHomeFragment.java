package com.lupefit.feature.configuracion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.feature.configuracion.R;
import com.lupefit.feature.configuracion.databinding.FragmentConfiguracionHomeBinding;

public class ConfiguracionHomeFragment extends Fragment {

    private FragmentConfiguracionHomeBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfiguracionHomeBinding.inflate(inflater, container, false);
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
        binding.optionCuenta.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_cuentaFragment));
        binding.optionPerfil.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_perfilFragment));
        binding.optionMetas.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_metasFragment));
        binding.optionNotificaciones.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_notificacionesFragment));
        binding.optionApariencia.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_aparienciaFragment));
        binding.optionUnidades.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_unidadesFragment));
        binding.optionSync.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_syncFragment));
        binding.optionExportar.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_exportarFragment));
        binding.optionCostos.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_costosFragment));
        binding.optionPresupuesto.setOnClickListener(v ->
                navController.navigate(R.id.action_configuracionHomeFragment_to_presupuestoFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
