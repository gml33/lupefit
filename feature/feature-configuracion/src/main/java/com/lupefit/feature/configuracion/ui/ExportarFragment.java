package com.lupefit.feature.configuracion.ui;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.time.LocalDate;
import java.util.function.Consumer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.feature.configuracion.databinding.FragmentExportarBinding;
import com.lupefit.feature.configuracion.viewmodel.ExportarViewModel;

public class ExportarFragment extends Fragment {

    private FragmentExportarBinding binding;
    private ExportarViewModel viewModel;
    private final ActivityResultLauncher<String> createCsv = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"),
            uri -> save(uri, ExportarViewModel.FormatoExportacion.CSV));
    private final ActivityResultLauncher<String> createJson = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("application/json"),
            uri -> save(uri, ExportarViewModel.FormatoExportacion.JSON));

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
        binding.etFechaInicio.setOnClickListener(v -> elegirFecha(viewModel.fechaInicio.getValue(),
                viewModel::setFechaInicio));
        binding.etFechaFin.setOnClickListener(v -> elegirFecha(viewModel.fechaFin.getValue(), viewModel::setFechaFin));
        binding.btnExportar.setOnClickListener(v -> {
            if (viewModel.fechasValidas()) {
                if (viewModel.formato.getValue() == ExportarViewModel.FormatoExportacion.JSON) {
                    createJson.launch("lupefit.json");
                } else {
                    createCsv.launch("lupefit.csv");
                }
            }
        });
    }

    private void save(Uri uri, ExportarViewModel.FormatoExportacion formato) {
        if (uri != null && viewModel != null) {
            viewModel.exportar(uri, formato);
        }
    }

    private void elegirFecha(String current, Consumer<String> save) {
        LocalDate initial = current == null || current.isEmpty() ? LocalDate.now() : LocalDate.parse(current);
        DatePickerDialog picker = new DatePickerDialog(requireContext(),
                (view, year, month, day) -> save.accept(LocalDate.of(year, month + 1, day).toString()),
                initial.getYear(), initial.getMonthValue() - 1, initial.getDayOfMonth());
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Quitar fecha", (dialog, button) -> save.accept(""));
        picker.show();
    }

    private void setupObservers() {
        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
        viewModel.archivoGenerado.observe(getViewLifecycleOwner(), archivo -> {
            if (archivo != null && !archivo.isEmpty()) {
                Toast.makeText(requireContext(), archivo, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}