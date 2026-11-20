package com.lupefit.app.ui.escaneo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.lupefit.app.R;
import com.lupefit.app.data.local.entity.FoodProduct;
import com.lupefit.app.data.local.repository.FoodCatalogRepository;
import com.lupefit.app.databinding.FragmentEscaneoCodigoBarrasBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fragment para escanear códigos de barras usando CameraX + ML Kit.
 * Maneja permisos de cámara, preview en tiempo real y detección de códigos.
 */
public class EscaneoCodigoBarrasFragment extends Fragment {

    private static final String TAG = "EscaneoCodigoBarras";
    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    private FragmentEscaneoCodigoBarrasBinding binding;
    private FoodCatalogRepository catalogRepository;
    private BarcodeAnalyzer barcodeAnalyzer;
    private ExecutorService cameraExecutor;
    private boolean hasCameraPermission = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        catalogRepository = new FoodCatalogRepository(
                requireContext(),
                ((com.lupefit.app.MyApplication) requireActivity().getApplication()).getContainer().getFoodProductDao()
        );
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEscaneoCodigoBarrasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        checkCameraPermission();
    }

    private void setupViews() {
        // Botón linterna
        binding.btnFlash.setOnClickListener(v -> toggleFlash());

        // Botón ingreso manual
        binding.btnManualEntry.setOnClickListener(v -> showManualEntryDialog());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = true;
            startCamera();
        } else {
            hasCameraPermission = false;
            showPermissionRationale();
        }
    }

    private void showPermissionRationale() {
        binding.previewView.setVisibility(View.GONE);
        binding.focusOverlay.setVisibility(View.GONE);
        binding.tvGuide.setText(R.string.escaneo_permiso_requerido);
        binding.btnFlash.setVisibility(View.GONE);
        binding.btnManualEntry.setText(R.string.escaneo_conceder_permiso);
        binding.btnManualEntry.setOnClickListener(v -> requestPermissions(
                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasCameraPermission = true;
                binding.tvGuide.setText(R.string.escaneo_guia_apunte_codigo);
                binding.btnFlash.setVisibility(View.VISIBLE);
                binding.btnManualEntry.setText(R.string.escaneo_ingreso_manual);
                binding.btnManualEntry.setOnClickListener(v -> showManualEntryDialog());
                startCamera();
            } else {
                Toast.makeText(requireContext(), R.string.escaneo_permiso_denegado, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCamera() {
        binding.previewView.setVisibility(View.VISIBLE);
        binding.focusOverlay.setVisibility(View.VISIBLE);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (Exception e) {
                Log.e(TAG, "Error iniciando cámara", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        // Preview
        Preview preview = new Preview.Builder()
                .setTargetRotation(requireActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        // ImageAnalysis para detección de códigos
        barcodeAnalyzer = new BarcodeAnalyzer(new BarcodeAnalyzer.BarcodeListener() {
            @Override
            public void onBarcodeDetected(String barcodeValue, int barcodeFormat) {
                requireActivity().runOnUiThread(() -> onBarcodeFound(barcodeValue));
            }

            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor, barcodeAnalyzer);

        // Seleccionar cámara trasera
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        // Unbind y bind
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis);
    }

    private void toggleFlash() {
        // TODO: Implementar toggle de linterna usando CameraControl
        // Requiere acceso a CameraControl desde ProcessCameraProvider
        Toast.makeText(requireContext(), R.string.escaneo_linterna_proximamente, Toast.LENGTH_SHORT).show();
    }

    private void onBarcodeFound(String barcode) {
        // Detener análisis temporalmente
        barcodeAnalyzer.reset();

        // Mostrar indicador de carga
        binding.scanningIndicator.setVisibility(View.VISIBLE);
        binding.scanningIndicator.animate()
                .translationX(0)
                .setDuration(800)
                .setStartDelay(0)
                .start();

        // Consultar catálogo
        catalogRepository.findByBarcode(barcode, product -> {
            requireActivity().runOnUiThread(() -> {
                binding.scanningIndicator.setVisibility(View.GONE);
                if (product != null) {
                    showResultSheet(product);
                } else {
                    showNotFoundDialog(barcode);
                }
            });
        });
    }

    private void showResultSheet(FoodProduct product) {
        ResultadoEscaneoSheet sheet = ResultadoEscaneoSheet.newInstance(product);
        sheet.show(getParentFragmentManager(), "ResultadoEscaneoSheet");
    }

    private void showNotFoundDialog(String barcode) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.escaneo_no_encontrado)
                .setMessage(getString(R.string.escaneo_no_encontrado_msg, barcode))
                .setPositiveButton(R.string.escaneo_crear_alimento, (dialog, which) ->
                        openCreateFoodFragment(barcode))
                .setNegativeButton(R.string.escaneo_reintentar, (dialog, which) ->
                        barcodeAnalyzer.reset())
                .show();
    }

    private void openCreateFoodFragment(String barcode) {
        // TODO: Navegar a DiarioCrearAlimentoFragment con barcode prellenado
        Toast.makeText(requireContext(), "Abrir crear alimento con código: " + barcode, Toast.LENGTH_SHORT).show();
    }

    private void showManualEntryDialog() {
        android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint(R.string.escaneo_hint_codigo);

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.escaneo_ingreso_manual)
                .setView(input)
                .setPositiveButton(R.string.escaneo_buscar, (dialog, which) -> {
                    String code = input.getText().toString().trim();
                    if (!code.isEmpty()) {
                        onBarcodeFound(code);
                    }
                })
                .setNegativeButton(R.string.escaneo_cancelar, null)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeAnalyzer != null) {
            barcodeAnalyzer.reset();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        if (catalogRepository != null) {
            catalogRepository.shutdown();
        }
    }
}