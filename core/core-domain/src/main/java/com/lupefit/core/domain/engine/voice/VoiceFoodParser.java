package com.lupefit.core.domain.engine.voice;

import com.lupefit.core.domain.model.FoodProduct;
import com.lupefit.core.domain.repository.FoodCatalogRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser nutricional para procesar frases naturales de voz.
 * Ejemplos:
 * - "200 gramos de pechuga de pollo, 100 gramos de arroz y 2 huevos"
 * - "una taza de avena con leche"
 * - "300g de salmón y ensalada"
 */
public class VoiceFoodParser {

    private static final String TAG = "VoiceFoodParser";

    private final FoodCatalogRepository catalogRepository;

    // Patrones para extraer cantidad y unidad
    private static final Pattern QUANTITY_PATTERN = Pattern.compile(
            "(?i)(\\d+(?:[.,]\\d+)?)\\s*(g|gr|gramos?|kg|kilos?|ml|cl|l|litros?|taza|tazas?|cda|cdas?|cucharada|cucharadas?|cdta|cdtas?|cucharadita|cucharaditas?|unidad|unidades?|u\\.?|pieza|piezas?|rebanada|rebanadas?|loncha|lonchas?)",
            Pattern.UNICODE_CASE
    );

    // Conectores para separar ítems
    private static final String[] CONNECTORS = {" y ", " e ", ", ", " con ", " más ", " plus "};

    // Palabras a ignorar (artículos, preposiciones)
    private static final List<String> STOP_WORDS = Arrays.asList(
            "de", "del", "la", "el", "los", "las", "un", "una", "unos", "unas",
            "con", "sin", "para", "por", "en", "a", "al", "mi", "mis", "tu", "tus",
            "su", "sus", "nuestro", "nuestra", "nuestros", "nuestras",
            "me", "te", "se", "nos", "le", "les", "lo", "la", "los", "las"
    );

    public VoiceFoodParser(FoodCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    /**
     * Parsea una frase natural y retorna lista de ParsedFoodItem.
     * Bloquea hasta completar (para uso en background thread).
     */
    public List<VoiceTranscriptionEngine.ParsedFoodItem> parse(String text) {
        List<VoiceTranscriptionEngine.ParsedFoodItem> results = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return results;
        }

        String cleanText = text.trim().toLowerCase(Locale.getDefault());

        // Separar por conectores
        String[] segments = splitByConnectors(cleanText);

        for (String segment : segments) {
            segment = segment.trim();
            if (segment.isEmpty()) continue;

            // Extraer cantidad y unidad
            ParsedSegment parsed = extractQuantityAndFood(segment);
            if (parsed == null) continue;

            // Buscar alimento en catálogo
            FoodProduct product = searchFood(parsed.foodName);
            if (product == null) {
                continue;
            }

            // Calcular gramos totales
            double grams = convertToGrams(parsed.quantity, parsed.unit, product);

            // Calcular macros
            FoodProduct.MacroBreakdown macros = product.calculateMacrosForGrams(grams);

            results.add(new VoiceTranscriptionEngine.ParsedFoodItem(
                    segment,
                    product.getName(),
                    parsed.quantity,
                    parsed.unit,
                    grams,
                    macros
            ));
        }

        return results;
    }

    /**
     * Separa el texto por conectores conocidos.
     */
    private String[] splitByConnectors(String text) {
        String result = text;
        for (String connector : CONNECTORS) {
            result = result.replace(connector, "|");
        }
        return result.split("\\|");
    }

    /**
     * Extrae cantidad, unidad y nombre del alimento de un segmento.
     */
    private ParsedSegment extractQuantityAndFood(String segment) {
        java.util.regex.Matcher matcher = QUANTITY_PATTERN.matcher(segment);
        if (matcher.find()) {
            String qtyStr = matcher.group(1).replace(',', '.');
            String unit = matcher.group(2).toLowerCase(Locale.getDefault());
            double quantity;
            try {
                quantity = Double.parseDouble(qtyStr);
            } catch (NumberFormatException e) {
                quantity = 1.0;
            }

            // El nombre del alimento es lo que queda después de la cantidad
            int endIdx = matcher.end();
            String foodName = segment.substring(endIdx).trim();

            // Limpiar palabras vacías del inicio
            foodName = cleanFoodName(foodName);

            if (foodName.isEmpty()) {
                // Si no hay nombre después, intentar antes de la cantidad
                foodName = segment.substring(0, matcher.start()).trim();
                foodName = cleanFoodName(foodName);
            }

            return new ParsedSegment(quantity, unit, foodName);
        }

        // Sin cantidad explícita, asumir 1 unidad
        String foodName = cleanFoodName(segment);
        if (!foodName.isEmpty()) {
            return new ParsedSegment(1.0, "unidad", foodName);
        }
        return null;
    }

    /**
     * Limpia el nombre del alimento quitando artículos y preposiciones.
     */
    private String cleanFoodName(String name) {
        String[] words = name.split("\\s+");
        StringBuilder cleaned = new StringBuilder();
        for (String word : words) {
            if (!STOP_WORDS.contains(word.toLowerCase(Locale.getDefault()))) {
                if (cleaned.length() > 0) cleaned.append(' ');
                cleaned.append(word);
            }
        }
        return cleaned.toString().trim();
    }

    /**
     * Busca el alimento en el catálogo (síncrono para uso en background).
     */
    private FoodProduct searchFood(String foodName) {
        final FoodProduct[] result = {null};
        final String[] error = {null};
        final CountDownLatch latch = new CountDownLatch(1);

        catalogRepository.searchByName(foodName, 5, (products, err) -> {
            error[0] = err;
            if (error[0] == null && !products.isEmpty()) {
                // Tomar el mejor match (el primero ya está ordenado por relevancia)
                result[0] = products.get(0);
            }
            latch.countDown();
        });

        try {
            latch.await(2, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return result[0];
    }

    /**
     * Convierte cantidad + unidad a gramos estimados.
     */
    private double convertToGrams(double quantity, String unit, FoodProduct product) {
        String u = unit.toLowerCase(Locale.getDefault());

        // Unidades de peso directo
        if (u.matches("g|gr|gramos?")) return quantity;
        if (u.matches("kg|kilos?")) return quantity * 1000;

        // Unidades de volumen (asumir densidad ~1g/ml para líquidos, ajustar para sólidos)
        if (u.matches("ml")) return quantity;
        if (u.matches("cl")) return quantity * 10;
        if (u.matches("l|litros?")) return quantity * 1000;

        // Medidas caseras
        if (u.matches("taza|tazas?")) return quantity * 240; // 1 taza ≈ 240ml
        if (u.matches("cda|cdas?|cucharada|cucharadas?")) return quantity * 15; // 1 cda ≈ 15ml
        if (u.matches("cdta|cdtas?|cucharadita|cucharaditas?")) return quantity * 5; // 1 cdta ≈ 5ml

        // Unidades por pieza
        if (u.matches("unidad|unidades?|u\\.?|pieza|piezas?")) {
            return quantity * product.getDefaultServingGrams();
        }
        if (u.matches("rebanada|rebanadas?|loncha|lonchas?")) {
            return quantity * 30; // ~30g por rebanada promedio
        }

        // Default: usar porción por defecto del producto
        return quantity * product.getDefaultServingGrams();
    }

    /**
     * Estructura interna para segmento parseado.
     */
    private static class ParsedSegment {
        final double quantity;
        final String unit;
        final String foodName;

        ParsedSegment(double quantity, String unit, String foodName) {
            this.quantity = quantity;
            this.unit = unit;
            this.foodName = foodName;
        }
    }
}