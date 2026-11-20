package com.lupefit.app.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Producto alimenticio para catálogo offline-first.
 * Incluye alimentos frescos (precargados) y productos escaneados de Open Food Facts.
 */
@Entity(
    tableName = "food_products",
    indices = {
        @Index(value = {"barcode"}, unique = true),
        @Index(value = {"name"}),
        @Index(value = {"is_fresh_food"})
    }
)
public final class FoodProduct implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "barcode")
    public String barcode; // null para alimentos frescos sin código de barras

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "brand")
    public String brand;

    @ColumnInfo(name = "kcal_per_100g")
    public double kcalPer100g;

    @ColumnInfo(name = "protein_per_100g")
    public double proteinPer100g;

    @ColumnInfo(name = "carbs_per_100g")
    public double carbsPer100g;

    @ColumnInfo(name = "fat_per_100g")
    public double fatPer100g;

    @ColumnInfo(name = "default_serving_grams")
    public double defaultServingGrams;

    @ColumnInfo(name = "is_fresh_food")
    public boolean isFreshFood;

    @ColumnInfo(name = "last_updated")
    public long lastUpdated;

    // Campos Offline-First (no sincronizados con backend, solo catálogo local)
    // No usamos isSynced/syncAction/serverId porque el catálogo es de solo lectura local

    public FoodProduct() {}

    protected FoodProduct(Parcel in) {
        id = in.readInt();
        barcode = in.readString();
        name = in.readString();
        brand = in.readString();
        kcalPer100g = in.readDouble();
        proteinPer100g = in.readDouble();
        carbsPer100g = in.readDouble();
        fatPer100g = in.readDouble();
        defaultServingGrams = in.readDouble();
        isFreshFood = in.readByte() != 0;
        lastUpdated = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(barcode);
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeDouble(kcalPer100g);
        dest.writeDouble(proteinPer100g);
        dest.writeDouble(carbsPer100g);
        dest.writeDouble(fatPer100g);
        dest.writeDouble(defaultServingGrams);
        dest.writeByte((byte) (isFreshFood ? 1 : 0));
        dest.writeLong(lastUpdated);
    }

    @Override
    public int describeContents() {
        return 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodProduct)) return false;
        FoodProduct that = (FoodProduct) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Calcula macros para una cantidad específica en gramos.
     */
    public MacroBreakdown calculateMacrosForGrams(double grams) {
        double factor = grams / 100.0;
        return new MacroBreakdown(
            proteinPer100g * factor,
            carbsPer100g * factor,
            fatPer100g * factor,
            kcalPer100g * factor
        );
    }

    /**
     * Estructura simple para desglose de macros.
     */
    public static final class MacroBreakdown {
        public final double proteinG;
        public final double carbsG;
        public final double fatG;
        public final double kcal;

        public MacroBreakdown(double proteinG, double carbsG, double fatG, double kcal) {
            this.proteinG = proteinG;
            this.carbsG = carbsG;
            this.fatG = fatG;
            this.kcal = kcal;
        }
    }
}