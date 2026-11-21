package com.lupefit.app.domain.engine.voice;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Implementación de VoiceTranscriptionEngine usando Whisper Server.
 * Graba audio localmente y lo envía a un endpoint configurable (/v1/audio/transcriptions).
 * Preparado para cuando el usuario active modo Whisper en Configuración.
 */
public class WhisperServerVoiceEngine implements VoiceTranscriptionEngine {

    private static final String TAG = "WhisperServerVoiceEngine";
    private static final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/wav");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");

    private final Context context;
    private final OkHttpClient httpClient;
    private final String serverUrl;
    private final String apiKey; // Opcional, para autenticación

    private Listener listener;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private volatile boolean isRecording = false;

    public WhisperServerVoiceEngine(Context context, String serverUrl, String apiKey) {
        this.context = context.getApplicationContext();
        this.serverUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
        this.apiKey = apiKey;

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void startListening(Listener listener) {
        if (isRecording) {
            Log.w(TAG, "Ya está grabando");
            return;
        }

        this.listener = listener;

        try {
            // Crear archivo temporal para audio
            File cacheDir = context.getCacheDir();
            audioFile = File.createTempFile("whisper_audio_", ".m4a", cacheDir);

            // Configurar MediaRecorder para MPEG-4 AAC 16kHz mono (compatibilidad amplia)
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioSamplingRate(16000);
            mediaRecorder.setAudioChannels(1);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;

            Log.d(TAG, "Grabación iniciada: " + audioFile.getAbsolutePath());

            // Notificar inicio (simular resultado parcial vacío)
            if (listener != null) {
                listener.onPartialResult("");
            }

        } catch (IOException e) {
            Log.e(TAG, "Error iniciando grabación", e);
            if (listener != null) {
                listener.onError("Error iniciando grabación: " + e.getMessage());
            }
            cleanup();
        }
    }

    @Override
    public void stopListening() {
        if (!isRecording || mediaRecorder == null) {
            return;
        }

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            isRecording = false;

            Log.d(TAG, "Grabación finalizada, enviando a Whisper...");

            // Enviar archivo a Whisper Server
            sendToWhisper();

        } catch (Exception e) {
            Log.e(TAG, "Error finalizando grabación", e);
            if (listener != null) {
                listener.onError("Error finalizando: " + e.getMessage());
            }
            cleanup();
        }
    }

    @Override
    public boolean isListening() {
        return isRecording;
    }

    @Override
    public void destroy() {
        stopListening();
        cleanup();
        httpClient.dispatcher().executorService().shutdown();
    }

    private void sendToWhisper() {
        if (audioFile == null || !audioFile.exists()) {
            if (listener != null) {
                listener.onError("Archivo de audio no encontrado");
            }
            cleanup();
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "audio.m4a", RequestBody.create(MEDIA_TYPE_AUDIO, audioFile))
                .addFormDataPart("model", "whisper-1") // Modelo por defecto
                .addFormDataPart("language", "es")
                .addFormDataPart("response_format", "json")
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(serverUrl + "v1/audio/transcriptions")
                .post(requestBody);

        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }

        Request request = requestBuilder.build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error enviando a Whisper", e);
                if (listener != null) {
                    listener.onError("Error de conexión: " + e.getMessage());
                }
                cleanup();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        String errorBody = response.body() != null ? response.body().string() : "";
                        Log.e(TAG, "Whisper HTTP " + response.code() + ": " + errorBody);
                        if (listener != null) {
                            listener.onError("Error servidor: HTTP " + response.code());
                        }
                        cleanup();
                        return;
                    }

                    String json = response.body() != null ? response.body().string() : "{}";
                    Log.d(TAG, "Respuesta Whisper: " + json);

                    // Parsear respuesta: {"text": "texto transcrito"}
                    com.google.gson.JsonObject obj = new com.google.gson.JsonParser().parse(json).getAsJsonObject();
                    String text = obj.has("text") ? obj.get("text").getAsString() : "";

                    if (listener != null) {
                        listener.onFinalResult(text.trim());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error parseando respuesta Whisper", e);
                    if (listener != null) {
                        listener.onError("Error procesando respuesta: " + e.getMessage());
                    }
                } finally {
                    cleanup();
                }
            }
        });
    }

    private void cleanup() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.release();
            } catch (Exception ignored) {}
            mediaRecorder = null;
        }
        if (audioFile != null && audioFile.exists()) {
            audioFile.delete();
            audioFile = null;
        }
        isRecording = false;
    }
}