package com.lupefit.core.domain.engine.voice;

import android.content.Context;

public class NativeAndroidVoiceEngine implements VoiceTranscriptionEngine {

    private final Context context;
    private android.speech.SpeechRecognizer speechRecognizer;
    private android.content.Intent recognizerIntent;
    private Listener listener;
    private volatile boolean isListening = false;

    public NativeAndroidVoiceEngine(Context context) {
        this.context = context.getApplicationContext();
        initializeRecognizer();
    }

    private void initializeRecognizer() {
        if (!android.speech.SpeechRecognizer.isRecognitionAvailable(context)) {
            return;
        }

        speechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(context);
        recognizerIntent = new android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE,
                java.util.Locale.getDefault());
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(android.speech.RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        speechRecognizer.setRecognitionListener(new android.speech.RecognitionListener() {
            @Override
            public void onReadyForSpeech(android.os.Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                String errorMsg = getErrorText(error);
                isListening = false;
                if (listener != null) {
                    listener.onError(errorMsg);
                }
            }

            @Override
            public void onResults(android.os.Bundle results) {
                isListening = false;
                java.util.ArrayList<String> matches = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty() && listener != null) {
                    listener.onFinalResult(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(android.os.Bundle partialResults) {
                java.util.ArrayList<String> matches = partialResults.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty() && listener != null) {
                    listener.onPartialResult(matches.get(0));
                }
            }

            @Override
            public void onEvent(int eventType, android.os.Bundle params) {}
        });
    }

    @Override
    public void startListening(Listener listener) {
        this.listener = listener;
        isListening = false;
        if (speechRecognizer != null) {
            speechRecognizer.startListening(recognizerIntent);
            isListening = true;
        } else if (listener != null) {
            listener.onError("Reconocimiento de voz no disponible en este dispositivo");
        }
    }

    @Override
    public void stopListening() {
        isListening = false;
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    @Override
    public boolean isListening() {
        return isListening;
    }

    @Override
    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        listener = null;
        isListening = false;
    }

    private String getErrorText(int errorCode) {
        switch (errorCode) {
            case android.speech.SpeechRecognizer.ERROR_AUDIO:
                return "Error de audio";
            case android.speech.SpeechRecognizer.ERROR_CLIENT:
                return "Error del cliente";
            case android.speech.SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "Permisos insuficientes";
            case android.speech.SpeechRecognizer.ERROR_NETWORK:
                return "Error de red";
            case android.speech.SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "Tiempo de espera de red agotado";
            case android.speech.SpeechRecognizer.ERROR_NO_MATCH:
                return "No se detectó voz";
            case android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "Reconocedor ocupado";
            case android.speech.SpeechRecognizer.ERROR_SERVER:
                return "Error del servidor";
            case android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "Tiempo de espera de voz agotado";
            default:
                return "Error desconocido: " + errorCode;
        }
    }
}