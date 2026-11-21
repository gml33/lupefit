package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FoodProduct implements Parcelable {

    private String id;
    private String barcode;
    private String name;
    private String brand;
    private double kcalPer100g;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatPer100g;
    private double fiberPer100g;
    private double sugarPer100g;
    private double sodiumPer100g;
    private double defaultServingGrams;
    private boolean isFreshFood;
    private long lastUpdated;
    // Cost tracking (Paso 4.5)
    private double referencePrice;
    private double referenceQuantityGrams;
    private long priceUpdatedAt;

    public FoodProduct() {}

    protected FoodProduct(Parcel in) {
        id = in.readString();
        barcode = in.readString();
        name = in.readString();
        brand = in.readString();
        kcalPer100g = in.readDouble();
        proteinPer100g = in.readDouble();
        carbsPer100g = in.readDouble();
        fatPer100g = in.readDouble();
        fiberPer100g = in.readDouble();
        sugarPer100g = in.readDouble();
        sodiumPer100g = in.readDouble();
        defaultServingGrams = in.readDouble();
        isFreshFood = in.readByte() != 0;
        lastUpdated = in.readLong();
        referencePrice = in.readDouble();
        referenceQuantityGrams = in.readDouble();
        priceUpdatedAt = in.readLong();
    }

    public static final Creator<FoodProduct> CREATOR = new Creator<FoodProduct>() {
        @Override
        public FoodProduct createFromParcel(Parcel in) {
            return new FoodProduct(in);
        }

        @Override
        public FoodProduct[] newArray(int size) {
            return new FoodProduct[size];
        }
    };

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getKcalPer100g() { return kcalPer100g; }
    public void setKcalPer100g(double kcalPer100g) { this.kcalPer100g = kcalPer100g; }

    public double getProteinPer100g() { return proteinPer100g; }
    public void setProteinPer100g(double proteinPer100g) { this.proteinPer100g = proteinPer100g; }

    public double getCarbsPer100g() { return carbsPer100g; }
    public void setCarbsPer100g(double carbsPer100g) { this.carbsPer100g = carbsPer100g; }

    public double getFatPer100g() { return fatPer100g; }
    public void setFatPer100g(double fatPer100g) { this.fatPer100g = fatPer100g; }

    public double getFiberPer100g() { return fiberPer100g; }
    public void setFiberPer100g(double fiberPer100g) { this.fiberPer100g = fiberPer100g; }

    public double getSugarPer100g() { return sugarPer100g; }
    public void setSugarPer100g(double sugarPer100g) { this.sugarPer100g = sugarPer100g; }

    public double getSodiumPer100g() { return sodiumPer100g; }
    public void setSodiumPer100g(double sodiumPer100g) { this.sodiumPer100g = sodiumPer100g; }

    public double getDefaultServingGrams() { return defaultServingGrams; }
    public void setDefaultServingGrams(double defaultServingGrams) { this.defaultServingGrams = defaultServingGrams; }

    public boolean isFreshFood() { return isFreshFood; }
    public void setFreshFood(boolean freshFood) { isFreshFood = freshFood; }

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    // Cost tracking (Paso 4.5)
    public double getReferencePrice() { return referencePrice; }
    public void setReferencePrice(double referencePrice) { this.referencePrice = referencePrice; }

    public double getReferenceQuantityGrams() { return referenceQuantityGrams; }
    public void setReferenceQuantityGrams(double referenceQuantityGrams) { this.referenceQuantityGrams = referenceQuantityGrams; }

    public long getPriceUpdatedAt() { return priceUpdatedAt; }
    public void setPriceUpdatedAt(long priceUpdatedAt) { this.priceUpdatedAt = priceUpdatedAt; }

    public double getCaloriesForGrams(double grams) {
        return (kcalPer100g / 100.0) * grams;
    }

    public double getProteinForGrams(double grams) {
        return (proteinPer100g / 100.0) * grams;
    }

    public double getCarbsForGrams(double grams) {
        return (carbsPer100g / 100.0) * grams;
    }

    public double getFatForGrams(double grams) {
        return (fatPer100g / 100.0) * grams;
    }

    /**
     * Calcula los macros para una cantidad específica en gramos.
     * Retorna un objeto MacroBreakdown con todos los valores.
     */
    public MacroBreakdown calculateMacrosForGrams(double grams) {
        return new MacroBreakdown(
                getCaloriesForGrams(grams),
                getProteinForGrams(grams),
                getCarbsForGrams(grams),
                getFatForGrams(grams),
                getFiberPer100g() / 100.0 * grams,
                getSugarPer100g() / 100.0 * grams,
                getSodiumPer100g() / 100.0 * grams,
                grams
        );
    }

    /**
     * Clase interna para desglose de macros.
     */
    public static class MacroBreakdown implements Parcelable {
        public final double kcal;
        public final double proteinG;
        public final double carbsG;
        public final double fatG;
        public final double fiberG;
        public final double sugarG;
        public final double sodiumMg;
        public final double grams;

        public MacroBreakdown(double kcal, double proteinG, double carbsG, double fatG,
                              double fiberG, double sugarG, double sodiumMg, double grams) {
            this.kcal = kcal;
            this.proteinG = proteinG;
            this.carbsG = carbsG;
            this.fatG = fatG;
            this.fiberG = fiberG;
            this.sugarG = sugarG;
            this.sodiumMg = sodiumMg;
            this.grams = grams;
        }

        protected MacroBreakdown(Parcel in) {
            kcal = in.readDouble();
            proteinG = in.readDouble();
            carbsG = in.readDouble();
            fatG = in.readDouble();
            fiberG = in.readDouble();
            sugarG = in.readDouble();
            sodiumMg = in.readDouble();
            grams = in.readDouble();
        }

        public static final Creator<MacroBreakdown> CREATOR = new Creator<MacroBreakdown>() {
            @Override
            public MacroBreakdown createFromParcel(Parcel in) {
                return new MacroBreakdown(in);
            }

            @Override
            public MacroBreakdown[] newArray(int size) {
                return new MacroBreakdown[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeDouble(kcal);
            dest.writeDouble(proteinG);
            dest.writeDouble(carbsG);
            dest.writeDouble(fatG);
            dest.writeDouble(fiberG);
            dest.writeDouble(sugarG);
            dest.writeDouble(sodiumMg);
            dest.writeDouble(grams);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(barcode);
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeDouble(kcalPer100g);
        dest.writeDouble(proteinPer100g);
        dest.writeDouble(carbsPer100g);
        dest.writeDouble(fatPer100g);
        dest.writeDouble(fiberPer100g);
        dest.writeDouble(sugarPer100g);
        dest.writeDouble(sodiumPer100g);
        dest.writeDouble(defaultServingGrams);
        dest.writeByte((byte) (isFreshFood ? 1 : 0));
        dest.writeLong(lastUpdated);
        dest.writeDouble(referencePrice);
        dest.writeDouble(referenceQuantityGrams);
        dest.writeLong(priceUpdatedAt);
    }
}