package com.lupefit.feature.nutricion.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lupefit.feature.nutricion.R;
import com.lupefit.feature.nutricion.databinding.FragmentMetasBinding;
import com.lupefit.feature.nutricion.viewmodel.MetasViewModel;

public class MetasFragment extends Fragment {

    private FragmentMetasBinding binding;
    private MetasViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMetasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        viewModel = new ViewModelProvider(this, new MetasViewModel.Factory(context)).get(MetasViewModel.class);

        setupToolbar();
        setupTextWatchers();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void setupTextWatchers() {
        // Calorías
        setupIntegerTextWatcher(binding.etMetaCalorias, viewModel.metaCalorias);
        // Proteínas
        setupFloatTextWatcher(binding.etMetaProteinas, viewModel.metaProteinas);
        // Carbohidratos
        setupFloatTextWatcher(binding.etMetaCarbohidratos, viewModel.metaCarbohidratos);
        // Grasas
        setupFloatTextWatcher(binding.etMetaGrasas, viewModel.metaGrasas);
        // Fibra
        setupFloatTextWatcher(binding.etMetaFibra, viewModel.metaFibra);
        // Agua
        setupIntegerTextWatcher(binding.etMetaAgua, viewModel.metaAgua);
    }

    private void setupIntegerTextWatcher(TextInputEditText editText, androidx.lifecycle.MutableLiveData<Integer> liveData) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (!text.isEmpty()) {
                    try {
                        liveData.setValue(Integer.parseInt(text));
                    } catch (NumberFormatException ignored) {}
                } else {
                    liveData.setValue(0);
                }
            }
        });
    }

    private void setupFloatTextWatcher(TextInputEditText editText, androidx.lifecycle.MutableLiveData<Float> liveData) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (!text.isEmpty()) {
                    try {
                        liveData.setValue(Float.parseFloat(text));
                    } catch (NumberFormatException ignored) {}
                } else {
                    liveData.setValue(0f);
                }
            }
        });
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