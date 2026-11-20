package com.lupefit.app.domain.engine.voice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Implementación nativa de VoiceTranscriptionEngine usando SpeechRecognizer de Android.
 * Configurado para español argentino (es-AR) con resultados parciales en tiempo real.
 */
public class NativeAndroidVoiceEngine implements VoiceTranscriptionEngine {

    private static final String TAG = "NativeAndroidVoiceEngine";

    private final Context context;
    private final SpeechRecognizer speechRecognizer;
    private Listener listener;
    private volatile boolean isListening = false;

    public NativeAndroidVoiceEngine(Context context) {
        this.context = context.getApplicationContext();

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.w(TAG, "SpeechRecognizer no disponible en este dispositivo");
            this.speechRecognizer = null;
            return;
        }

        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(android.os.Bundle params) {
                Log.d(TAG, "Listo para escuchar");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "Inicio de habla detectado");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Nivel de audio en dB, útil para animación visual
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "Fin de habla detectado");
            }

            @Override
            public void onError(int error) {
                String errorMsg = getErrorMessage(error);
                Log.w(TAG, "Error SpeechRecognizer: " + errorMsg);
                if (listener != null) {
                    listener.onError(errorMsg);
                }
                isListening = false;
            }

            @Override
            public void onResults(android.os.Bundle results) {
                List<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty() && listener != null) {
                    String finalText = matches.get(0);
                    Log.d(TAG, "Resultado final: " + finalText);
                    listener.onFinalResult(finalText);
                }
                isListening = false;
            }

            @Override
            public void onPartialResults(android.os.Bundle partialResults) {
                List<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty() && listener != null) {
                    String partialText = matches.get(0);
                    Log.d(TAG, "Resultado parcial: " + partialText);
                    listener.onPartialResult(partialText);
                }
            }

            @Override
            public void onEvent(int eventType, android.os.Bundle params) {
            }
        });
    }

    @Override
    public void startListening(Listener listener) {
        if (speechRecognizer == null) {
            if (listener != null) {
                listener.onError("Reconocimiento de voz no disponible en este dispositivo");
            }
            return;
        }

        if (isListening) {
            Log.w(TAG, "Ya está escuchando");
            return;
        }

        this.listener = listener;
        isListening = true;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-AR"); // Español argentino
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-AR");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500);

        try {
            speechRecognizer.startListening(intent);
            Log.d(TAG, "Iniciando escucha...");
        } catch (Exception e) {
            Log.e(TAG, "Error iniciando reconocimiento", e);
            isListening = false;
            if (listener != null) {
                listener.onError("Error iniciando: " + e.getMessage());
            }
        }
    }

    @Override
    public void stopListening() {
        if (speechRecognizer != null && isListening) {
            speechRecognizer.stopListening();
            isListening = false;
            Log.d(TAG, "Escucha detenida");
        }
    }

    @Override
    public boolean isListening() {
        return isListening;
    }

    @Override
    public void destroy() {
        stopListening();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    private String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO: return "Error de audio";
            case SpeechRecognizer.ERROR_CLIENT: return "Error del cliente";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: return "Permisos insuficientes";
            case SpeechRecognizer.ERROR_NETWORK: return "Error de red";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: return "Timeout de red";
            case SpeechRecognizer.ERROR_NO_MATCH: return "No se reconoció nada";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: return "Reconocedor ocupado";
            case SpeechRecognizer.ERROR_SERVER: return "Error del servidor";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: return "Timeout de voz";
            default: return "Error desconocido (" + errorCode + ")";
        }
    }
}