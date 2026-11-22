package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class IngredienteReceta implements Parcelable {

    private long id;
    private long recetaId;
    private long alimentoId;
    private String alimentoNombre;
    private float cantidad;
    private String unidad;
    private String nota;
    private int orden;

    // Transient fields for UI
    private transient String cantidadStr;

    public IngredienteReceta() {}

    protected IngredienteReceta(Parcel in) {
        id = in.readLong();
        recetaId = in.readLong();
        alimentoId = in.readLong();
        alimentoNombre = in.readString();
        cantidad = in.readFloat();
        unidad = in.readString();
        nota = in.readString();
        orden = in.readInt();
    }

    public static final Creator<IngredienteReceta> CREATOR = new Creator<IngredienteReceta>() {
        @Override
        public IngredienteReceta createFromParcel(Parcel in) {
            return new IngredienteReceta(in);
        }

        @Override
        public IngredienteReceta[] newArray(int size) {
            return new IngredienteReceta[size];
        }
    };

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getRecetaId() { return recetaId; }
    public void setRecetaId(long recetaId) { this.recetaId = recetaId; }

    public long getAlimentoId() { return alimentoId; }
    public void setAlimentoId(long alimentoId) { this.alimentoId = alimentoId; }

    public String getAlimentoNombre() { return alimentoNombre; }
    public void setAlimentoNombre(String alimentoNombre) { this.alimentoNombre = alimentoNombre; }

    public float getCantidad() { return cantidad; }
    public void setCantidad(float cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    // Formatted string for UI
    public String getCantidadStr() {
        if (cantidadStr == null) {
            cantidadStr = String.valueOf(cantidad);
        }
        return cantidadStr;
    }

    public void setCantidadStr(String cantidadStr) {
        this.cantidadStr = cantidadStr;
        try {
            this.cantidad = Float.parseFloat(cantidadStr);
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(recetaId);
        dest.writeLong(alimentoId);
        dest.writeString(alimentoNombre);
        dest.writeFloat(cantidad);
        dest.writeString(unidad);
        dest.writeString(nota);
        dest.writeInt(orden);
    }
}