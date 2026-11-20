package com.lupefit.core.network.dto.user;

import com.squareup.moshi.Json;

/** Metas de macronutrientes */
public final class MacroTargets {
    @Json(name = "protein_g")
    public final int proteinG;

    @Json(name = "carbs_g")
    public final int carbsG;

    @Json(name = "fat_g")
    public final int fatG;

    @Json(name = "protein_pct")
    public final int proteinPct;

    @Json(name = "carbs_pct")
    public final int carbsPct;

    @Json(name = "fat_pct")
    public final int fatPct;

    public MacroTargets(int proteinG, int carbsG, int fatG,
                       int proteinPct, int carbsPct, int fatPct) {
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
        this.proteinPct = proteinPct;
        this.carbsPct = carbsPct;
        this.fatPct = fatPct;
    }
}