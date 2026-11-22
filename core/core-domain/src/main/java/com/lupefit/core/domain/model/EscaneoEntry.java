package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EscaneoEntry implements Parcelable {

    private long id;
    private String tipo; // "codigo_barras" | "comida_ia"
    private String productoNombre;
    private String marca;
    private String codigoBarras;
    private String fotoUri;
    private String fecha;
    private long timestamp;
    private boolean agregadoAlDiario;
    private long diarioEntryId;

    // Nutrición (para código de barras)
    private int caloriasPor100g;
    private float proteinasPor100g;
    private float carbohidratosPor100g;
    private float grasasPor100g;

    // Para comida IA - items detectados
    private String alimentosDetectadosJson;

    // Sync fields
    private boolean isSynced = false;
    private String syncAction = "CREATE";
    private String serverId;
    private long lastModified = System.currentTimeMillis();

    public EscaneoEntry() {}

    protected EscaneoEntry(Parcel in) {
        id = in.readLong();
        tipo = in.readString();
        productoNombre = in.readString();
        marca = in.readString();
        codigoBarras = in.readString();
        fotoUri = in.readString();
        fecha = in.readString();
        timestamp = in.readLong();
        agregadoAlDiario = in.readByte() != 0;
        diarioEntryId = in.readLong();
        caloriasPor100g = in.readInt();
        proteinasPor100g = in.readFloat();
        carbohidratosPor100g = in.readFloat();
        grasasPor100g = in.readFloat();
        alimentosDetectadosJson = in.readString();
        isSynced = in.readByte() != 0;
        syncAction = in.readString();
        serverId = in.readString();
        lastModified = in.readLong();
    }

    public static final Creator<EscaneoEntry> CREATOR = new Creator<EscaneoEntry>() {
        @Override
        public EscaneoEntry createFromParcel(Parcel in) {
            return new EscaneoEntry(in);
        }

        @Override
        public EscaneoEntry[] newArray(int size) {
            return new EscaneoEntry[size];
        }
    };

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getFotoUri() { return fotoUri; }
    public void setFotoUri(String fotoUri) { this.fotoUri = fotoUri; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isAgregadoAlDiario() { return agregadoAlDiario; }
    public void setAgregadoAlDiario(boolean agregadoAlDiario) { this.agregadoAlDiario = agregadoAlDiario; }

    public long getDiarioEntryId() { return diarioEntryId; }
    public void setDiarioEntryId(long diarioEntryId) { this.diarioEntryId = diarioEntryId; }

    public int getCaloriasPor100g() { return caloriasPor100g; }
    public void setCaloriasPor100g(int caloriasPor100g) { this.caloriasPor100g = caloriasPor100g; }

    public float getProteinasPor100g() { return proteinasPor100g; }
    public void setProteinasPor100g(float proteinasPor100g) { this.proteinasPor100g = proteinasPor100g; }

    public float getCarbohidratosPor100g() { return carbohidratosPor100g; }
    public void setCarbohidratosPor100g(float carbohidratosPor100g) { this.carbohidratosPor100g = carbohidratosPor100g; }

    public float getGrasasPor100g() { return grasasPor100g; }
    public void setGrasasPor100g(float grasasPor100g) { this.grasasPor100g = grasasPor100g; }

    public String getAlimentosDetectadosJson() { return alimentosDetectadosJson; }
    public void setAlimentosDetectadosJson(String alimentosDetectadosJson) { this.alimentosDetectadosJson = alimentosDetectadosJson; }

    public boolean isSynced() { return isSynced; }
    public void setSynced(boolean synced) { isSynced = synced; }

    public String getSyncAction() { return syncAction; }
    public void setSyncAction(String syncAction) { this.syncAction = syncAction; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(tipo);
        dest.writeString(productoNombre);
        dest.writeString(marca);
        dest.writeString(codigoBarras);
        dest.writeString(fotoUri);
        dest.writeString(fecha);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (agregadoAlDiario ? 1 : 0));
        dest.writeLong(diarioEntryId);
        dest.writeInt(caloriasPor100g);
        dest.writeFloat(proteinasPor100g);
        dest.writeFloat(carbohidratosPor100g);
        dest.writeFloat(grasasPor100g);
        dest.writeString(alimentosDetectadosJson);
        dest.writeByte((byte) (isSynced ? 1 : 0));
        dest.writeString(syncAction);
        dest.writeString(serverId);
        dest.writeLong(lastModified);
    }
}