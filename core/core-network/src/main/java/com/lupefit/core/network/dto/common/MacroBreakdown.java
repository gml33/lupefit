package com.lupefit.core.network.dto.common;

import com.squareup.moshi.Json;

/** Desglose de macronutrientes */
public final class MacroBreakdown {
    @Json(name = "protein_g")
    public final double proteinG;

    @Json(name = "carbs_g")
    public final double carbsG;

    @Json(name = "fat_g")
    public final double fatG;

    @Json(name = "fiber_g")
    public final double fiberG;

    @Json(name = "sugar_g")
    public final double sugarG;

    @Json(name = "sodium_mg")
    public final double sodiumMg;

    public MacroBreakdown(double proteinG, double carbsG, double fatG,
                         double fiberG, double sugarG, double sodiumMg) {
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
        this.fiberG = fiberG;
        this.sugarG = sugarG;
        this.sodiumMg = sodiumMg;
    }
}