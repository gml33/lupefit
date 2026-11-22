package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class WaterEntry implements Parcelable {

    private long id;
    private int cantidadMl;
    private String fecha;
    private String hora;
    private long timestamp;
    private int tipoVaso; // 0=personalizado, 1=vaso 250ml, 2=botella 500ml, etc.

    // Transient fields for UI
    private transient String cantidadStr;
    private transient String horaStr;

    public WaterEntry() {}

    protected WaterEntry(Parcel in) {
        id = in.readLong();
        cantidadMl = in.readInt();
        fecha = in.readString();
        hora = in.readString();
        timestamp = in.readLong();
        tipoVaso = in.readInt();
    }

    public static final Creator<WaterEntry> CREATOR = new Creator<WaterEntry>() {
        @Override
        public WaterEntry createFromParcel(Parcel in) {
            return new WaterEntry(in);
        }

        @Override
        public WaterEntry[] newArray(int size) {
            return new WaterEntry[size];
        }
    };

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getCantidadMl() { return cantidadMl; }
    public void setCantidadMl(int cantidadMl) { this.cantidadMl = cantidadMl; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getTipoVaso() { return tipoVaso; }
    public void setTipoVaso(int tipoVaso) { this.tipoVaso = tipoVaso; }

    // Formatted strings for UI
    public String getCantidadStr() {
        if (cantidadStr == null) {
            cantidadStr = cantidadMl + " ml";
        }
        return cantidadStr;
    }

    public void setCantidadStr(String cantidadStr) { this.cantidadStr = cantidadStr; }

    public String getHoraStr() {
        if (horaStr == null && hora != null) {
            horaStr = hora;
        }
        return horaStr != null ? horaStr : "";
    }

    public void setHoraStr(String horaStr) { this.horaStr = horaStr; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(cantidadMl);
        dest.writeString(fecha);
        dest.writeString(hora);
        dest.writeLong(timestamp);
        dest.writeInt(tipoVaso);
    }
}