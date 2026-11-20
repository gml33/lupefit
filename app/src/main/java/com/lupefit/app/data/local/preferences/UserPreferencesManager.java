package com.lupefit.app.data.local.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gestor de preferencias de usuario con SharedPreferences.
 * Incluye feature toggles opcionales como el seguimiento de costos.
 */
public final class UserPreferencesManager {

    private static final String PREF_NAME = "lupefit_user_preferences";
    private static final String KEY_ENABLE_COST_TRACKING = "enable_cost_tracking";
    private static final String KEY_WEEKLY_BUDGET_GOAL = "weekly_budget_goal";
    private static final String KEY_CURRENCY_CODE = "currency_code";

    private final SharedPreferences prefs;

    private UserPreferencesManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static UserPreferencesManager getInstance(Context context) {
        return new UserPreferencesManager(context);
    }

    // ==================== FEATURE TOGGLE: COST TRACKING ====================

    /**
     * Indica si el usuario ha activado el seguimiento de costos.
     * Por defecto: true (activado).
     */
    public boolean isCostTrackingEnabled() {
        return prefs.getBoolean(KEY_ENABLE_COST_TRACKING, true);
    }

    /**
     * Activa o desactiva el seguimiento de costos globalmente.
     * Cuando se desactiva, todos los campos y vistas de dinero se ocultan (View.GONE).
     */
    public void setCostTrackingEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_ENABLE_COST_TRACKING, enabled).apply();
    }

    // ==================== PRESUPUESTO SEMANAL ====================

    /**
     * Obtiene la meta de presupuesto semanal en la moneda local.
     * 0.0 significa que no hay meta definida.
     */
    public double getWeeklyBudgetGoal() {
        // SharedPreferences no soporta double directamente, usamos long como workaround
        long bits = prefs.getLong(KEY_WEEKLY_BUDGET_GOAL, 0L);
        return bits == 0L ? 0.0 : Double.longBitsToDouble(bits);
    }

    /**
     * Establece la meta de presupuesto semanal.
     * @param goal Valor en moneda local (ej: 5000.0 para $5000 ARS/semana)
     */
    public void setWeeklyBudgetGoal(double goal) {
        long bits = goal == 0.0 ? 0L : Double.doubleToLongBits(goal);
        prefs.edit().putLong(KEY_WEEKLY_BUDGET_GOAL, bits).apply();
    }

    // ==================== MONEDA ====================

    /**
     * Código ISO 4217 de la moneda (ej: "ARS", "USD", "EUR").
     * Por defecto: "ARS" (Peso Argentino).
     */
    public String getCurrencyCode() {
        return prefs.getString(KEY_CURRENCY_CODE, "ARS");
    }

    /**
     * Establece el código de moneda.
     */
    public void setCurrencyCode(String currencyCode) {
        prefs.edit().putString(KEY_CURRENCY_CODE, currencyCode).apply();
    }

    // ==================== UTILIDADES ====================

    /**
     * Limpia todas las preferencias (útil para tests o logout).
     */
    public void clearAll() {
        prefs.edit().clear().apply();
    }
}