package com.lupefit.app.domain.engine.voice;

import com.lupefit.app.data.local.entity.FoodProduct;
import java.util.List;

/**
 * Motor de transcripción de voz - Patrón Strategy.
 * Permite intercambiar entre SpeechRecognizer nativo y Whisper Server.
 */
public interface VoiceTranscriptionEngine {

    /**
     * Inicia la escucha de voz.
     * @param listener Callback para resultados parciales, finales y errores
     */
    void startListening(Listener listener);

    /**
     * Detiene la escucha.
     */
    void stopListening();

    /**
     * Verifica si está escuchando actualmente.
     */
    boolean isListening();

    /**
     * Libera recursos.
     */
    void destroy();

    /**
     * Callback para resultados de transcripción.
     */
    interface Listener {
        /**
         * Resultado parcial (en tiempo real mientras habla el usuario).
         */
        void onPartialResult(String partialText);

        /**
         * Resultado final (cuando el usuario deja de hablar).
         */
        void onFinalResult(String finalText);

        /**
         * Error durante la transcripción.
         */
        void onError(String error);
    }

    /**
     * Item de alimento parseado desde voz.
     */
    final class ParsedFoodItem {
        public final String originalText;
        public final String foodName;
        public final double quantity;
        public final String unit;
        public final double grams;
        public final FoodProduct.MacroBreakdown macros;

        public ParsedFoodItem(String originalText, String foodName, double quantity,
                              String unit, double grams, FoodProduct.MacroBreakdown macros) {
            this.originalText = originalText;
            this.foodName = foodName;
            this.quantity = quantity;
            this.unit = unit;
            this.grams = grams;
            this.macros = macros;
        }
    }
}