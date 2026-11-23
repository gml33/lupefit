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
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupClickListeners() {
        binding.switchPush.setOnClickListener(v -> viewModel.setPushEnabled(binding.switchPush.isChecked()));
        binding.switchResumenSemanal.setOnClickListener(v -> viewModel.setResumenSemanal(binding.switchResumenSemanal.isChecked()));
        binding.switchLogros.setOnClickListener(v -> viewModel.setLogros(binding.switchLogros.isChecked()));
        binding.switchRecordatorioAgua.setOnClickListener(v -> viewModel.setRecordatorioAgua(binding.switchRecordatorioAgua.isChecked()));
        binding.switchRecordatorioComida.setOnClickListener(v -> viewModel.setRecordatorioComida(binding.switchRecordatorioComida.isChecked()));
        binding.switchRecordatorioEjercicio.setOnClickListener(v -> viewModel.setRecordatorioEjercicio(binding.switchRecordatorioEjercicio.isChecked()));
        binding.switchRecordatorioPeso.setOnClickListener(v -> viewModel.setRecordatorioPeso(binding.switchRecordatorioPeso.isChecked()));
        binding.etHoraAgua.setOnClickListener(v -> elegirHora(viewModel.horaRecordatorioAgua.getValue(),
                viewModel::setHoraRecordatorioAgua));
        binding.etHoraComida.setOnClickListener(v -> elegirHora(viewModel.horaRecordatorioComida.getValue(),
                viewModel::setHoraRecordatorioComida));
    }

    private void elegirHora(String hora, java.util.function.Consumer<String> save) {
        String[] parts = hora.split(":");
        new android.app.TimePickerDialog(requireContext(), (dialog, hour, minute) ->
                save.accept(String.format(java.util.Locale.ROOT, "%02d:%02d", hour, minute)),
                Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), true).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}