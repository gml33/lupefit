package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MacroTotals implements Parcelable {

    private int calorias;
    private double proteinas;
    private double carbohidratos;
    private double grasas;
    private double costoEstimado;

    public MacroTotals() {}

    public MacroTotals(int calorias, double proteinas, double carbohidratos, double grasas) {
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.costoEstimado = 0.0;
    }

    public MacroTotals(int calorias, double proteinas, double carbohidratos, double grasas, double costoEstimado) {
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.costoEstimado = costoEstimado;
    }

    protected MacroTotals(Parcel in) {
        calorias = in.readInt();
        proteinas = in.readDouble();
        carbohidratos = in.readDouble();
        grasas = in.readDouble();
        costoEstimado = in.readDouble();
    }

    public static final Creator<MacroTotals> CREATOR = new Creator<MacroTotals>() {
        @Override
        public MacroTotals createFromParcel(Parcel in) {
            return new MacroTotals(in);
        }

        @Override
        public MacroTotals[] newArray(int size) {
            return new MacroTotals[size];
        }
    };

    // Getters and Setters
    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public double getProteinas() { return proteinas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }

    public double getCarbohidratos() { return carbohidratos; }
    public void setCarbohidratos(double carbohidratos) { this.carbohidratos = carbohidratos; }

    public double getGrasas() { return grasas; }
    public void setGrasas(double grasas) { this.grasas = grasas; }

    public double getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(double costoEstimado) { this.costoEstimado = costoEstimado; }

    public double getPorcentajeProteinas() {
        if (calorias <= 0) return 0;
        return (proteinas * 4.0 / calorias) * 100;
    }

    public double getPorcentajeCarbohidratos() {
        if (calorias <= 0) return 0;
        return (carbohidratos * 4.0 / calorias) * 100;
    }

    public double getPorcentajeGrasas() {
        if (calorias <= 0) return 0;
        return (grasas * 9.0 / calorias) * 100;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(calorias);
        dest.writeDouble(proteinas);
        dest.writeDouble(carbohidratos);
        dest.writeDouble(grasas);
        dest.writeDouble(costoEstimado);
    }
}