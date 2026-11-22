package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PesoEntry implements Parcelable {

    private long id;
    private float peso;
    private String fecha;
    private String nota;
    private int dateKey;
    private long timestamp;
    private double diferencia;

    // Transient fields for UI display (not persisted)
    private transient String pesoStr;
    private transient String diffStr;

    public PesoEntry() {}

    protected PesoEntry(Parcel in) {
        id = in.readLong();
        peso = in.readFloat();
        fecha = in.readString();
        nota = in.readString();
        dateKey = in.readInt();
        timestamp = in.readLong();
        diferencia = in.readDouble();
    }

    public static final Creator<PesoEntry> CREATOR = new Creator<PesoEntry>() {
        @Override
        public PesoEntry createFromParcel(Parcel in) {
            return new PesoEntry(in);
        }

        @Override
        public PesoEntry[] newArray(int size) {
            return new PesoEntry[size];
        }
    };

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public float getPeso() { return peso; }
    public void setPeso(float peso) { this.peso = peso; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }

    public int getDateKey() { return dateKey; }
    public void setDateKey(int dateKey) { this.dateKey = dateKey; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getDiferencia() { return diferencia; }
    public void setDiferencia(double diferencia) { this.diferencia = diferencia; }

    // Formatted strings for UI
    public String getPesoStr() {
        if (pesoStr == null) {
            pesoStr = String.format(java.util.Locale.getDefault(), "%.1f kg", peso);
        }
        return pesoStr;
    }

    public void setPesoStr(String pesoStr) { this.pesoStr = pesoStr; }

    public String getDiffStr() {
        if (diffStr == null) {
            diffStr = String.format(java.util.Locale.getDefault(), "%s%.1f kg",
                    diferencia >= 0 ? "+" : "", diferencia);
        }
        return diffStr;
    }

    public void setDiffStr(String diffStr) { this.diffStr = diffStr; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(peso);
        dest.writeString(fecha);
        dest.writeString(nota);
        dest.writeInt(dateKey);
        dest.writeLong(timestamp);
        dest.writeDouble(diferencia);
    }
}