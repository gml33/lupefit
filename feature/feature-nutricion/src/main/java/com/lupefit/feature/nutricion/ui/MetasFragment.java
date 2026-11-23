package com.lupefit.feature.nutricion.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.lupefit.feature.nutricion.R;
import com.lupefit.feature.nutricion.databinding.FragmentMetasNutricionBinding;
import com.lupefit.feature.nutricion.viewmodel.MetasViewModel;

public class MetasFragment extends Fragment {

    private FragmentMetasNutricionBinding binding;
    private MetasViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMetasNutricionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        viewModel = new ViewModelProvider(this, new MetasViewModel.Factory(context)).get(MetasViewModel.class);

        setupToolbar();
        initializeFields();
        binding.btnGuardar.setOnClickListener(v -> save());
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void initializeFields() {
        binding.etMetaCalorias.setText(String.valueOf(viewModel.metaCalorias.getValue()));
        binding.etMetaProteinas.setText(String.valueOf(viewModel.metaProteinas.getValue()));
        binding.etMetaCarbohidratos.setText(String.valueOf(viewModel.metaCarbohidratos.getValue()));
        binding.etMetaGrasas.setText(String.valueOf(viewModel.metaGrasas.getValue()));
        binding.etMetaFibra.setText(String.valueOf(viewModel.metaFibra.getValue()));
        binding.etMetaAgua.setText(String.valueOf(viewModel.metaAgua.getValue()));
        binding.switchNetCarbs.setChecked(Boolean.TRUE.equals(viewModel.netCarbsMode.getValue()));
    }

    private void save() {
        try {
            int calorias = Math.round(readAmount(binding.etMetaCalorias));
            float proteinas = readAmount(binding.etMetaProteinas);
            float carbohidratos = readAmount(binding.etMetaCarbohidratos);
            float grasas = readAmount(binding.etMetaGrasas);
            float fibra = readAmount(binding.etMetaFibra);
            int agua = Math.round(readAmount(binding.etMetaAgua));
            viewModel.metaCalorias.setValue(calorias);
            viewModel.metaProteinas.setValue(proteinas);
            viewModel.metaCarbohidratos.setValue(carbohidratos);
            viewModel.metaGrasas.setValue(grasas);
            viewModel.metaFibra.setValue(fibra);
            viewModel.metaAgua.setValue(agua);
            viewModel.netCarbsMode.setValue(binding.switchNetCarbs.isChecked());
            viewModel.saveMetas();
        } catch (NumberFormatException ignored) {
            Toast.makeText(requireContext(), R.string.metas_valores_invalidos, Toast.LENGTH_SHORT).show();
        }
    }

    private float readAmount(TextInputEditText field) {
        String text = field.getText() == null ? "" : field.getText().toString().trim();
        float value = Float.parseFloat(text.replace(',', '.'));
        if (!Float.isFinite(value) || value < 0) {
            throw new NumberFormatException();
        }
        return value;
    }

    private void setupObservers() {
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (saved != null && saved) {
                Toast.makeText(requireContext(), "Metas guardadas", Toast.LENGTH_SHORT).show();
                // Use postValue since we're on main thread but LiveData.setValue is protected
                ((androidx.lifecycle.MutableLiveData<Boolean>) viewModel.saved).postValue(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}