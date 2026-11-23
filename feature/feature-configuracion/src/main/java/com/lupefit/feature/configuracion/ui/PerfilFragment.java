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

import com.lupefit.feature.configuracion.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupefit.feature.configuracion.databinding.FragmentPerfilBinding;
import com.lupefit.feature.configuracion.viewmodel.PerfilConfigViewModel;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilConfigViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new PerfilConfigViewModel.Factory(requireContext())).get(PerfilConfigViewModel.class);
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
        binding.btnGuardarPerfil.setOnClickListener(v -> {
            viewModel.setNombre(binding.etNombre.getText().toString().trim());
            viewModel.setApellido(binding.etApellido.getText().toString().trim());
            viewModel.setAltura(leerDecimal(binding.etAltura.getText().toString().trim()));
            viewModel.setPeso(leerDecimal(binding.etPeso.getText().toString().trim()));
            viewModel.setGrasaCorporal(leerDecimal(binding.etGrasaCorporal.getText().toString().trim()));
            viewModel.savePerfil();
        });

        binding.etFechaNacimiento.setOnClickListener(v -> elegirFecha());
        binding.etSexo.setOnClickListener(v -> elegirOpcion(binding.etSexo.getHint(),
                R.array.lupefit_config_sexos, viewModel::setSexo));
        binding.etNivelActividad.setOnClickListener(v -> {
            String[] niveles = getResources().getStringArray(R.array.lupefit_config_niveles_actividad);
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(binding.etNivelActividad.getHint())
                    .setItems(niveles, (dialog, position) -> viewModel.setNivelActividad(position + 1))
                    .show();
        });
        binding.etObjetivo.setOnClickListener(v -> elegirOpcion(binding.etObjetivo.getHint(),
                R.array.lupefit_config_objetivos, viewModel::setObjetivo));
    }

    private void elegirFecha() {
        java.time.LocalDate inicial = java.time.LocalDate.now();
        String fecha = viewModel.fechaNacimiento.getValue();
        if (fecha != null && !fecha.isEmpty()) {
            try {
                inicial = java.time.LocalDate.parse(fecha);
            } catch (java.time.format.DateTimeParseException ignored) {
                // Se puede reemplazar un valor legado desde el selector.
            }
        }
        android.app.DatePickerDialog picker = new android.app.DatePickerDialog(requireContext(),
                (view, year, month, day) -> viewModel.setFechaNacimiento(
                        java.time.LocalDate.of(year, month + 1, day).toString()),
                inicial.getYear(), inicial.getMonthValue() - 1, inicial.getDayOfMonth());
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show();
    }

    private void elegirOpcion(CharSequence title, int opciones, java.util.function.Consumer<String> guardar) {
        String[] opcionesTexto = getResources().getStringArray(opciones);
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setItems(opcionesTexto, (dialog, position) -> guardar.accept(opcionesTexto[position]))
                .show();
    }

    private static float leerDecimal(String value) {
        try {
            return Float.parseFloat(value.replace(',', '.'));
        } catch (NumberFormatException invalid) {
            return 0f;
        }
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Perfil guardado", Toast.LENGTH_SHORT).show();
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