package com.lupefit.app.domain.engine.budget;

import com.lupefit.app.data.local.entity.FoodProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Motor de cálculo para estimación de costos y presupuestación de menús.
 * Provee métodos para calcular costos por porción, costo por proteína,
 * detectar precios desactualizados y resumir presupuestos semanales.
 */
public final class CostBudgetEngine {

    private static final long PRICE_STALE_THRESHOLD_MS = 30L * 24 * 60 * 60 * 1000; // 30 días

    private CostBudgetEngine() {}

    /**
     * Calcula el costo estimado de una porción consumida.
     * Fórmula: (referencePrice / referenceQuantityGrams) * consumedGrams
     *
     * @param product Producto con precio de referencia cargado
     * @param consumedGrams Gramos consumidos en esta porción
     * @return Costo estimado, o 0.0 si no hay precio válido
     */
    public static double calculateServingCost(FoodProduct product, double consumedGrams) {
        if (product == null) return 0.0;
        if (product.referencePrice <= 0.0) return 0.0;
        if (product.referenceQuantityGrams <= 0.0) return 0.0;
        if (consumedGrams <= 0.0) return 0.0;

        double pricePerGram = product.referencePrice / product.referenceQuantityGrams;
        return pricePerGram * consumedGrams;
    }

    /**
     * Verifica si el precio de referencia está desactualizado (>30 días).
     * Útil para alertar al usuario en contextos inflacionarios.
     *
     * @param priceUpdatedAt Timestamp de última actualización del precio
     * @return true si el precio tiene más de 30 días
     */
    public static boolean isPriceOutdated(long priceUpdatedAt) {
        if (priceUpdatedAt <= 0) return true; // Sin fecha = desactualizado
        long now = System.currentTimeMillis();
        return (now - priceUpdatedAt) > PRICE_STALE_THRESHOLD_MS;
    }

    /**
     * Calcula días transcurridos desde la última actualización del precio.
     *
     * @param priceUpdatedAt Timestamp de última actualización
     * @return Días transcurridos, o -1 si no hay fecha
     */
    public static int getDaysSincePriceUpdate(long priceUpdatedAt) {
        if (priceUpdatedAt <= 0) return -1;
        long now = System.currentTimeMillis();
        long diffMs = now - priceUpdatedAt;
        return (int) (diffMs / (24 * 60 * 60 * 1000));
    }

    /**
     * Calcula cuánto cuesta obtener 10 gramos de proteína pura de un alimento.
     * Fórmula: (calculateServingCost(product, 100g) / proteinPer100g) * 10
     * Esencial para el motor de sustituciones (FoodSwapEngine) ordenar por eficiencia proteica.
     *
     * @param product Producto con precio y macros
     * @return Costo por 10g de proteína, o 0.0 si no hay precio o proteína
     */
    public static double calculateCostPer10gProtein(FoodProduct product) {
        if (product == null) return 0.0;
        if (product.referencePrice <= 0.0) return 0.0;
        if (product.referenceQuantityGrams <= 0.0) return 0.0;
        if (product.proteinPer100g <= 0.0) return 0.0; // Sin proteína = no aplica

        double costPer100g = calculateServingCost(product, 100.0);
        double proteinPer100g = product.proteinPer100g;

        return (costPer100g / proteinPer100g) * 10.0;
    }

    /**
     * Calcula el costo por 100g del producto (precio normalizado).
     *
     * @param product Producto con precio de referencia
     * @return Costo por 100g, o 0.0 si no hay precio
     */
    public static double calculateCostPer100g(FoodProduct product) {
        return calculateServingCost(product, 100.0);
    }

    /**
     * Formatea un costo como moneda local.
     *
     * @param cost Valor numérico
     * @param currencyCode Código ISO 4217 (ej: "ARS")
     * @return String formateado (ej: "$6.850")
     */
    public static String formatCost(double cost, String currencyCode) {
        try {
            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
            format.setCurrency(java.util.Currency.getInstance(currencyCode));
            format.setMaximumFractionDigits(0);
            return format.format(cost);
        } catch (Exception e) {
            // Fallback simple
            return String.format(Locale.getDefault(), "$%.0f", cost);
        }
    }

    /**
     * Calcula resumen de presupuesto para un menú semanal planificado.
     *
     * @param items Lista de items planificados (cada uno con producto y gramos)
     * @param weeklyBudgetGoal Meta de presupuesto semanal (0 = sin meta)
     * @return BudgetSummary con totales, promedio diario, items sin precio, estado vs meta
     */
    public static BudgetSummary calculateWeeklyMenuBudget(
            List<PlannedMealItem> items,
            double weeklyBudgetGoal) {

        double totalEstimatedCost = 0.0;
        int missingPriceCount = 0;
        List<PlannedMealItem> itemsWithoutPrice = new ArrayList<>();

        for (PlannedMealItem item : items) {
            double itemCost = calculateServingCost(item.product, item.plannedGrams);
            if (itemCost > 0) {
                totalEstimatedCost += itemCost;
            } else {
                missingPriceCount++;
                itemsWithoutPrice.add(item);
            }
        }

        double averageDailyCost = totalEstimatedCost / 7.0;
        boolean withinBudget = weeklyBudgetGoal <= 0 || totalEstimatedCost <= weeklyBudgetGoal;
        double budgetRemaining = weeklyBudgetGoal > 0 ? weeklyBudgetGoal - totalEstimatedCost : 0.0;
        double budgetPercentage = weeklyBudgetGoal > 0 ? (totalEstimatedCost / weeklyBudgetGoal) * 100.0 : 0.0;

        return new BudgetSummary(
                totalEstimatedCost,
                averageDailyCost,
                missingPriceCount,
                itemsWithoutPrice,
                weeklyBudgetGoal,
                withinBudget,
                budgetRemaining,
                budgetPercentage
        );
    }

    /**
     * Item de comida planificada para cálculo de presupuesto semanal.
     */
    public static final class PlannedMealItem {
        public final FoodProduct product;
        public final double plannedGrams;
        public final String mealType; // "DESAYUNO", "ALMUERZO", etc.
        public final int dayOfWeek; // 1=Lunes ... 7=Domingo

        public PlannedMealItem(FoodProduct product, double plannedGrams, String mealType, int dayOfWeek) {
            this.product = product;
            this.plannedGrams = plannedGrams;
            this.mealType = mealType;
            this.dayOfWeek = dayOfWeek;
        }
    }

    /**
     * Resumen de presupuesto semanal calculado.
     */
    public static final class BudgetSummary {
        public final double totalEstimatedCost;
        public final double averageDailyCost;
        public final int missingPriceCount;
        public final List<PlannedMealItem> itemsWithoutPrice;
        public final double weeklyBudgetGoal;
        public final boolean withinBudget;
        public final double budgetRemaining;
        public final double budgetPercentage;

        public BudgetSummary(double totalEstimatedCost,
                             double averageDailyCost,
                             int missingPriceCount,
                             List<PlannedMealItem> itemsWithoutPrice,
                             double weeklyBudgetGoal,
                             boolean withinBudget,
                             double budgetRemaining,
                             double budgetPercentage) {
            this.totalEstimatedCost = totalEstimatedCost;
            this.averageDailyCost = averageDailyCost;
            this.missingPriceCount = missingPriceCount;
            this.itemsWithoutPrice = itemsWithoutPrice;
            this.weeklyBudgetGoal = weeklyBudgetGoal;
            this.withinBudget = withinBudget;
            this.budgetRemaining = budgetRemaining;
            this.budgetPercentage = budgetPercentage;
        }

        /**
         * Texto descriptivo del estado del presupuesto.
         */
        public String getStatusText(String currencyCode) {
            if (weeklyBudgetGoal <= 0) {
                return "Sin meta de presupuesto";
            }
            if (withinBudget) {
                return String.format("Dentro del presupuesto (%s restantes)", formatCost(budgetRemaining, currencyCode));
            } else {
                return String.format("Excede presupuesto por %s", formatCost(-budgetRemaining, currencyCode));
            }
        }

        /**
         * Texto para items sin precio.
         */
        public String getMissingPriceText() {
            if (missingPriceCount == 0) {
                return "Todos los items tienen precio";
            } else if (missingPriceCount == 1) {
                return "1 ingrediente sin precio cargado";
            } else {
                return missingPriceCount + " ingredientes sin precio cargado";
            }
        }
    }
}