package com.lupefit.app.ui.escaneo;

import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

/**
 * Analizador de códigos de barras usando Google ML Kit.
 * Configurado para formatos EAN-13, EAN-8, UPC-A, UPC-E.
 * Incluye bloqueo anti-duplicados para evitar lecturas repetidas.
 */
public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

    private static final String TAG = "BarcodeAnalyzer";

    private final BarcodeScanner barcodeScanner;
    private final BarcodeListener listener;

    // Bloqueo anti-duplicados
    private volatile boolean isProcessing = false;
    private volatile long lastDetectionTime = 0;
    private static final long DEDUP_WINDOW_MS = 1500; // 1.5 segundos entre detecciones

    public interface BarcodeListener {
        void onBarcodeDetected(String barcodeValue, int barcodeFormat);
        void onError(String error);
    }

    public BarcodeAnalyzer(BarcodeListener listener) {
        this.listener = listener;

        // Configurar scanner para formatos de productos
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_EAN_13 |
                        Barcode.FORMAT_EAN_8 |
                        Barcode.FORMAT_UPC_A |
                        Barcode.FORMAT_UPC_E
                )
                .build();

        this.barcodeScanner = BarcodeScanning.getClient(options);
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        // Anti-duplicado: ignorar si estamos procesando o si pasó poco tiempo
        long now = System.currentTimeMillis();
        if (isProcessing || (now - lastDetectionTime) < DEDUP_WINDOW_MS) {
            imageProxy.close();
            return;
        }

        isProcessing = true;

        try {
            @OptIn(markerClass = ExperimentalGetImage.class)
            androidx.camera.core.ImageProxy.PlaneProxy[] planes = imageProxy.getPlanes();
            if (planes == null || planes.length == 0) {
                imageProxy.close();
                isProcessing = false;
                return;
            }

            // Crear InputImage desde ImageProxy
            InputImage image = InputImage.fromMediaImage(
                    imageProxy.getImage(),
                    imageProxy.getImageInfo().getRotationDegrees()
            );

            // Procesar con ML Kit
            barcodeScanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        if (!barcodes.isEmpty()) {
                            Barcode barcode = barcodes.get(0);
                            String rawValue = barcode.getRawValue();
                            int format = barcode.getFormat();

                            if (rawValue != null && !rawValue.isEmpty()) {
                                lastDetectionTime = System.currentTimeMillis();
                                Log.d(TAG, "Código detectado: " + rawValue + " (formato: " + format + ")");
                                listener.onBarcodeDetected(rawValue, format);
                            }
                        }
                        imageProxy.close();
                        isProcessing = false;
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error escaneando código", e);
                        listener.onError(e.getMessage());
                        imageProxy.close();
                        isProcessing = false;
                    })
                    .addOnCanceledListener(() -> {
                        imageProxy.close();
                        isProcessing = false;
                    });

        } catch (Exception e) {
            Log.e(TAG, "Error en analyze", e);
            listener.onError(e.getMessage());
            imageProxy.close();
            isProcessing = false;
        }
    }

    /**
     * Libera recursos del scanner.
     */
    public void close() {
        barcodeScanner.close();
    }

    /**
     * Resetea el estado de procesamiento (útil al reanudar fragment).
     */
    public void reset() {
        isProcessing = false;
        lastDetectionTime = 0;
    }
}