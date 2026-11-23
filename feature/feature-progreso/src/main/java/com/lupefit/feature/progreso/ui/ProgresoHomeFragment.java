package com.lupefit.feature.progreso.ui;

import android.os.Bundle;
import android.net.Uri;
import android.content.ContentResolver;
import android.Manifest;
import android.content.pm.PackageManager;
import android.text.InputType;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupefit.core.domain.model.PesoEntry;
import com.lupefit.core.domain.provider.ProgresoRepositoryProvider;
import com.lupefit.core.domain.repository.ProgresoRepository;
import com.lupefit.feature.progreso.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.File;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.lupefit.feature.progreso.databinding.FragmentProgresoHomeBinding;
import com.lupefit.feature.progreso.viewmodel.ProgresoHomeViewModel;

public class ProgresoHomeFragment extends Fragment {

    private FragmentProgresoHomeBinding binding;
    private ProgresoHomeViewModel viewModel;
    private NavController navController;
    private ProgresoRepository repository;
    private final ActivityResultLauncher<String> csvExport = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"), uri -> export(uri, false));
    private final ActivityResultLauncher<String> jsonExport = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("application/json"), uri -> export(uri, true));

    private static final int[] MEASURES = {R.string.medidas_pecho, R.string.medidas_cintura,
            R.string.medidas_cadera, R.string.medidas_brazo_izq, R.string.medidas_brazo_der,
            R.string.medidas_muslo_izq, R.string.medidas_muslo_der, R.string.medidas_pantorrilla_izq,
            R.string.medidas_pantorrilla_der, R.string.medidas_cuello, R.string.medidas_hombro};
    private static final String[] MEASURE_IDS = {"pecho", "cintura", "cadera", "brazo_izq", "brazo_der",
            "muslo_izq", "muslo_der", "pantorrilla_izq", "pantorrilla_der", "cuello", "hombro"};
    private File pendingPhoto;

    private final ActivityResultLauncher<Uri> camera = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), success -> {
                File photo = pendingPhoto;
                pendingPhoto = null;
                if (photo != null && success && photo.length() > 0) {
                    Toast.makeText(requireContext(), R.string.progress_photo_saved, Toast.LENGTH_SHORT).show();
                } else if (photo != null) {
                    photo.delete();
                }
            });

    private final ActivityResultLauncher<String> cameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    takeProgressPhoto();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgresoHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getString("progressPhoto") != null) {
            pendingPhoto = new File(photoDirectory(), savedInstanceState.getString("progressPhoto"));
        }
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(ProgresoHomeViewModel.class);
        repository = ((ProgresoRepositoryProvider) requireContext().getApplicationContext())
                .getProgresoRepository();

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        // Card: Registrar Peso
        binding.cardRegistrarPeso.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToPesoFragment()));

        // Card: Registrar Medidas
        binding.cardRegistrarMedidas.setOnClickListener(v -> showMeasurements());

        // Card: Tomar Foto
        binding.cardTomarFoto.setOnClickListener(v -> showPhotos());

        // Card: Ver Gráficos
        binding.cardVerGraficos.setOnClickListener(v -> showWeightChart());

        // Button: Exportar
        binding.btnExportar.setOnClickListener(v -> chooseExportFormat());

        // Button: Ver Gráficos (bottom)
        binding.btnVerGraficos.setOnClickListener(v -> showWeightChart());

        // Empty state button
        binding.emptyState.btnFirstWeight.setOnClickListener(v ->
                navController.navigate(ProgresoHomeFragmentDirections.Companion.actionProgresoHomeFragmentToPesoFragment()));
    }

    private void showMeasurements() {
        List<ProgresoRepository.Measurement> entries = viewModel.getMeasurements().getValue();
        StringBuilder summary = new StringBuilder();
        if (entries != null) {
            for (ProgresoRepository.Measurement entry : entries) {
                int index = Arrays.asList(MEASURE_IDS).indexOf(entry.kind);
                if (index >= 0) {
                    summary.append(getString(R.string.progress_measure_row, getString(MEASURES[index]),
                            entry.centimeters, DateFormat.getDateInstance().format(new Date(entry.timestamp))))
                            .append('\n');
                }
            }
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.medidas_corporales)
                .setMessage(summary.length() == 0 ? getString(R.string.progress_no_measurements) : summary)
                .setPositiveButton(R.string.progreso_registrar_medidas, (dialog, which) -> chooseMeasurement())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void chooseMeasurement() {
        CharSequence[] labels = new CharSequence[MEASURES.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = getText(MEASURES[i]);
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.medidas_corporales)
                .setItems(labels, (dialog, index) -> editMeasurement(index))
                .show();
    }

    private void editMeasurement(int index) {
        com.google.android.material.textfield.TextInputLayout field =
                new com.google.android.material.textfield.TextInputLayout(requireContext());
        field.setHint(getString(R.string.medidas_cm));
        com.google.android.material.textfield.TextInputEditText input =
                new com.google.android.material.textfield.TextInputEditText(field.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        field.addView(input);
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(MEASURES[index])
                .setView(field)
                .setPositiveButton(R.string.medidas_guardar, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    double value;
                    try {
                        value = Double.parseDouble(String.valueOf(input.getText()).replace(',', '.'));
                    } catch (NumberFormatException error) {
                        value = 0;
                    }
                    if (!Double.isFinite(value) || value <= 0 || value > 500) {
                        field.setError(getString(R.string.progress_invalid_measure));
                        return;
                    }
                    viewModel.saveMeasurement(repository, MEASURE_IDS[index], value);
                    dialog.dismiss();
                }));
        dialog.show();
    }

    private File photoDirectory() {
        return new File(requireContext().getFilesDir(), "progress_photos");
    }

    private void showPhotos() {
        File[] files = photoDirectory().listFiles(file -> file.length() > 0);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.fotos_progreso)
                .setPositiveButton(R.string.progreso_tomar_foto, (d, i) -> {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        takeProgressPhoto();
                    } else {
                        cameraPermission.launch(Manifest.permission.CAMERA);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        if (files == null || files.length == 0) {
            dialog.setMessage(R.string.progress_no_photos);
        } else {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            CharSequence[] dates = new CharSequence[files.length];
            for (int i = 0; i < files.length; i++) {
                dates[i] = DateFormat.getDateTimeInstance().format(new Date(files[i].lastModified()));
            }
            dialog.setItems(dates, (d, index) -> {
                ImageView image = new ImageView(requireContext());
                image.setAdjustViewBounds(true);
                image.setImageURI(Uri.fromFile(files[index]));
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(dates[index])
                        .setView(image)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            });
        }
        dialog.show();
    }

    private void takeProgressPhoto() {
        try {
            File directory = photoDirectory();
            if (!directory.isDirectory() && !directory.mkdirs()) {
                throw new IOException("Directorio de fotos inaccesible");
            }
            pendingPhoto = File.createTempFile("progreso-", ".jpg", directory);
            camera.launch(FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".progress.photos", pendingPhoto));
        } catch (IOException | RuntimeException error) {
            if (pendingPhoto != null) {
                pendingPhoto.delete();
                pendingPhoto = null;
            }
            Toast.makeText(requireContext(), R.string.progress_photo_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (pendingPhoto != null) {
            outState.putString("progressPhoto", pendingPhoto.getName());
        }
    }

    private void setupObservers() {
        viewModel.getWeights().observe(getViewLifecycleOwner(), weights -> {
            binding.emptyState.getRoot().setVisibility(weights.isEmpty() ? View.VISIBLE : View.GONE);
            binding.cardCurrentWeight.setVisibility(weights.isEmpty() ? View.GONE : View.VISIBLE);
            if (weights.isEmpty()) {
                return;
            }
            float current = weights.get(0).getPeso();
            float initial = weights.get(weights.size() - 1).getPeso();
            binding.tvCurrentWeight.setText(getString(R.string.progress_weight_value, current));
            binding.tvInitialWeight.setText(getString(R.string.progress_weight_value, initial));
            binding.tvWeightDiff.setText(getString(R.string.progress_weight_delta, current - initial));
        });
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Toast.makeText(requireContext(), R.string.progress_read_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh(repository);
        viewModel.refreshMeasurements(repository);
    }

    private void showWeightChart() {
        List<PesoEntry> weights = viewModel.getWeights().getValue();
        if (weights == null || weights.isEmpty()) {
            Toast.makeText(requireContext(), R.string.progress_no_weights, Toast.LENGTH_SHORT).show();
            return;
        }
        List<Entry> points = new ArrayList<>();
        for (int index = weights.size() - 1; index >= 0; index--) {
            points.add(new Entry(points.size(), weights.get(index).getPeso()));
        }
        int ink = com.google.android.material.color.MaterialColors.getColor(
                binding.getRoot(), com.google.android.material.R.attr.colorOnSurface);
        int accent = com.google.android.material.color.MaterialColors.getColor(
                binding.getRoot(), androidx.appcompat.R.attr.colorPrimary);
        LineChart chart = new LineChart(requireContext());
        chart.setMinimumHeight((int) (240 * getResources().getDisplayMetrics().density));
        chart.getDescription().setEnabled(false);
        chart.getLegend().setTextColor(ink);
        chart.getAxisLeft().setTextColor(ink);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawLabels(false);
        LineDataSet data = new LineDataSet(points, getString(R.string.graficos_peso));
        data.setColor(accent);
        data.setCircleColor(accent);
        data.setValueTextColor(ink);
        chart.setData(new LineData(data));
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.graficos_progreso)
                .setView(chart)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void chooseExportFormat() {
        List<PesoEntry> weights = viewModel.getWeights().getValue();
        if (weights == null || weights.isEmpty()) {
            Toast.makeText(requireContext(), R.string.progress_no_weights, Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.exportar_formato)
                .setItems(new CharSequence[]{getString(R.string.exportar_csv), getString(R.string.exportar_json)},
                        (dialog, option) -> {
                            if (option == 0) {
                                csvExport.launch("lupefit-peso.csv");
                            } else {
                                jsonExport.launch("lupefit-peso.json");
                            }
                        }).show();
    }

    private void export(Uri uri, boolean json) {
        if (uri == null) {
            return;
        }
        List<PesoEntry> weights = viewModel.getWeights().getValue();
        if (weights == null || weights.isEmpty()) {
            Toast.makeText(requireContext(), R.string.progress_no_weights, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentResolver resolver = requireContext().getContentResolver();
        try (OutputStream output = resolver.openOutputStream(uri)) {
            if (output == null) {
                throw new IOException("Documento inaccesible");
            }
            StringBuilder csv = new StringBuilder("fecha,peso_kg\n");
            JSONArray array = new JSONArray();
            for (PesoEntry entry : weights) {
                if (json) {
                    array.put(new JSONObject().put("fecha", entry.getFecha()).put("peso_kg", entry.getPeso()));
                } else {
                    csv.append(entry.getFecha()).append(',').append(entry.getPeso()).append('\n');
                }
            }
            output.write((json ? array.toString() : csv.toString()).getBytes(StandardCharsets.UTF_8));
            Toast.makeText(requireContext(), R.string.progress_export_done, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.progress_export_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
