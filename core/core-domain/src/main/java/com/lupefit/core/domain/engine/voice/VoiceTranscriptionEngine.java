package com.lupefit.core.domain.engine.voice;

import com.lupefit.core.domain.model.FoodProduct;

import java.util.List;

public interface VoiceTranscriptionEngine {

    interface Listener {
        void onPartialResult(String partialText);
        void onFinalResult(String finalText);
        void onError(String error);
    }

    void startListening(Listener listener);
    void stopListening();
    boolean isListening();
    void destroy();

    class ParsedFoodItem {
        private final String originalText;
        private final String foodName;
        private final double quantity;
        private final String unit;
        private final double grams;
        private final FoodProduct.MacroBreakdown macros;

        public ParsedFoodItem(String originalText, String foodName, double quantity, String unit, double grams, FoodProduct.MacroBreakdown macros) {
            this.originalText = originalText;
            this.foodName = foodName;
            this.quantity = quantity;
            this.unit = unit;
            this.grams = grams;
            this.macros = macros;
        }

        public String getOriginalText() { return originalText; }
        public String getFoodName() { return foodName; }
        public double getQuantity() { return quantity; }
        public String getUnit() { return unit; }
        public double getGrams() { return grams; }
        public FoodProduct.MacroBreakdown getMacros() { return macros; }
    }
}