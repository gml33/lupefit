package com.lupefit.core.network.dto.food;

import com.squareup.moshi.Json;

/** Micronutrientes detallados */
public final class Micronutrients {
    @Json(name = "fiber_g")
    public final Double fiberG;

    @Json(name = "sugar_g")
    public final Double sugarG;

    @Json(name = "sodium_mg")
    public final Double sodiumMg;

    @Json(name = "potassium_mg")
    public final Double potassiumMg;

    @Json(name = "calcium_mg")
    public final Double calciumMg;

    @Json(name = "iron_mg")
    public final Double ironMg;

    @Json(name = "vitamin_c_mg")
    public final Double vitaminCMg;

    @Json(name = "vitamin_d_mcg")
    public final Double vitaminDMcg;

    @Json(name = "vitamin_b12_mcg")
    public final Double vitaminB12Mcg;

    @Json(name = "magnesium_mg")
    public final Double magnesiumMg;

    @Json(name = "zinc_mg")
    public final Double zincMg;

    public Micronutrients(Double fiberG, Double sugarG, Double sodiumMg,
                         Double potassiumMg, Double calciumMg, Double ironMg,
                         Double vitaminCMg, Double vitaminDMcg, Double vitaminB12Mcg,
                         Double magnesiumMg, Double zincMg) {
        this.fiberG = fiberG;
        this.sugarG = sugarG;
        this.sodiumMg = sodiumMg;
        this.potassiumMg = potassiumMg;
        this.calciumMg = calciumMg;
        this.ironMg = ironMg;
        this.vitaminCMg = vitaminCMg;
        this.vitaminDMcg = vitaminDMcg;
        this.vitaminB12Mcg = vitaminB12Mcg;
        this.magnesiumMg = magnesiumMg;
        this.zincMg = zincMg;
    }
}