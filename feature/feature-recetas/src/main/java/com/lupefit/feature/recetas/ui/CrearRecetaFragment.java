package com.lupefit.feature.recetas.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.feature.recetas.R;
import com.lupefit.feature.recetas.databinding.FragmentCrearRecetaBinding;
import com.lupefit.feature.recetas.databinding.ItemIngredienteBinding;
import com.lupefit.feature.recetas.viewmodel.CrearRecetaViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrearRecetaFragment extends Fragment {

    private FragmentCrearRecetaBinding binding;
    private CrearRecetaViewModel viewModel;
    private final ActivityResultLauncher<String[]> gallery = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri == null || viewModel == null) return;
                try {
                    requireContext().getContentResolver().takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    viewModel.fotoUri.setValue(uri.toString());
                } catch (SecurityException e) {
                    showPhotoError();
                }
            });
    private final ActivityResultLauncher<Void> camera = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(), this::saveCameraPreview);
    private final ActivityResultLauncher<String> cameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), accepted -> {
                if (accepted) openCamera();
                else showPhotoError();
            });

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle state) {
        binding = FragmentCrearRecetaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle state) {
        viewModel = new ViewModelProvider(this, new CrearRecetaViewModel.Factory(requireContext()))
                .get(CrearRecetaViewModel.class);
        if (getArguments() != null) viewModel.loadRecipe(getArguments().getParcelable("receta"));
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.btnAgregarIngrediente.setOnClickListener(v -> viewModel.addIngrediente());
        binding.btnGaleria.setOnClickListener(v -> gallery.launch(new String[]{"image/*"}));
        binding.btnTomarFoto.setOnClickListener(v -> {
            if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                cameraPermission.launch(Manifest.permission.CAMERA);
            }
        });
        setOptions(binding.etCategoria, R.string.categoria_desayuno, R.string.categoria_almuerzo,
                R.string.categoria_cena, R.string.categoria_snack, R.string.categoria_postre,
                R.string.categoria_bebida, R.string.categoria_otro);
        setOptions(binding.etDificultad, R.string.dificultad_facil, R.string.dificultad_media,
                R.string.dificultad_dificil);

        IngredientAdapter adapter = new IngredientAdapter();
        binding.rvIngredientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvIngredientes.setAdapter(adapter);
        binding.rvIngredientes.setNestedScrollingEnabled(false);
        viewModel.ingredientes.observe(getViewLifecycleOwner(), adapter::replace);
        viewModel.isSaving.observe(getViewLifecycleOwner(), saving -> binding.btnGuardarReceta.setEnabled(!saving));
        viewModel.saved.observe(getViewLifecycleOwner(), saved -> {
            if (Boolean.TRUE.equals(saved)) NavHostFragment.findNavController(this).navigateUp();
        });
        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
        viewModel.fotoUri.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null && !uri.isEmpty()) binding.ivRecetaPhoto.setImageURI(Uri.parse(uri));
        });
    }

    private void setOptions(MaterialAutoCompleteTextView field, int... labels) {
        List<String> values = new ArrayList<>();
        for (int label : labels) values.add(getString(label));
        field.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, values));
    }

    private void openCamera() {
        try {
            camera.launch(null);
        } catch (android.content.ActivityNotFoundException e) {
            showPhotoError();
        }
    }

    private void saveCameraPreview(Bitmap bitmap) {
        if (bitmap == null || viewModel == null) return;
        try {
            File photo = File.createTempFile("receta-", ".jpg", requireContext().getFilesDir());
            try (FileOutputStream stream = new FileOutputStream(photo)) {
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)) {
                    throw new IOException();
                }
            }
            viewModel.fotoUri.setValue(Uri.fromFile(photo).toString());
        } catch (IOException e) {
            showPhotoError();
        }
    }

    private void showPhotoError() {
        if (isAdded()) Toast.makeText(requireContext(), R.string.receta_error_foto, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class IngredientAdapter extends RecyclerView.Adapter<IngredientHolder> {
        private List<IngredienteReceta> items = new ArrayList<>();

        void replace(List<IngredienteReceta> ingredients) {
            items = ingredients;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemIngredienteBinding row = ItemIngredienteBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            setOptions(row.etIngredienteUnidad, R.string.unidad_g, R.string.unidad_kg,
                    R.string.unidad_ml, R.string.unidad_l, R.string.unidad_cda, R.string.unidad_cdt,
                    R.string.unidad_taza, R.string.unidad_unidad, R.string.unidad_pizca,
                    R.string.unidad_al_gusto);
            row.setViewModel(viewModel);
            return new IngredientHolder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
            holder.row.setIngrediente(items.get(position));
            holder.row.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class IngredientHolder extends RecyclerView.ViewHolder {
        final ItemIngredienteBinding row;

        IngredientHolder(ItemIngredienteBinding row) {
            super(row.getRoot());
            this.row = row;
        }
    }
}
