package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DiarioEntry implements Parcelable {

    private String id;
    private String foodName;
    private String foodId;
    private String foodProductId;
    private double cantidad;
    private int calorias;
    private double proteinas;
    private double carbohidratos;
    private double grasas;
    private double fibra;
    private double azucar;
    private double sodio;
    private String tipoComida;
    private long timestamp;
    private int dateKey;
    private double estimatedCostSnapshot;
    private String notes;
    private String barcode;
    private boolean fromVoiceInput;
    private boolean fromAiFoodScan;
    private String sourceType;
    private boolean isSynced;
    private String syncAction;
    private String serverId;
    private long lastModified;

    public DiarioEntry() {}

    protected DiarioEntry(Parcel in) {
        id = in.readString();
        foodName = in.readString();
        foodId = in.readString();
        foodProductId = in.readString();
        cantidad = in.readDouble();
        calorias = in.readInt();
        proteinas = in.readDouble();
        carbohidratos = in.readDouble();
        grasas = in.readDouble();
        fibra = in.readDouble();
        azucar = in.readDouble();
        sodio = in.readDouble();
        tipoComida = in.readString();
        timestamp = in.readLong();
        dateKey = in.readInt();
        estimatedCostSnapshot = in.readDouble();
        notes = in.readString();
        barcode = in.readString();
        fromVoiceInput = in.readByte() != 0;
        fromAiFoodScan = in.readByte() != 0;
        sourceType = in.readString();
        isSynced = in.readByte() != 0;
        syncAction = in.readString();
        serverId = in.readString();
        lastModified = in.readLong();
    }

    public static final Creator<DiarioEntry> CREATOR = new Creator<DiarioEntry>() {
        @Override
        public DiarioEntry createFromParcel(Parcel in) {
            return new DiarioEntry(in);
        }

        @Override
        public DiarioEntry[] newArray(int size) {
            return new DiarioEntry[size];
        }
    };

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getFoodProductId() { return foodProductId; }
    public void setFoodProductId(String foodProductId) { this.foodProductId = foodProductId; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public double getProteinas() { return proteinas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }

    public double getCarbohidratos() { return carbohidratos; }
    public void setCarbohidratos(double carbohidratos) { this.carbohidratos = carbohidratos; }

    public double getGrasas() { return grasas; }
    public void setGrasas(double grasas) { this.grasas = grasas; }

    public double getFibra() { return fibra; }
    public void setFibra(double fibra) { this.fibra = fibra; }

    public double getAzucar() { return azucar; }
    public void setAzucar(double azucar) { this.azucar = azucar; }

    public double getSodio() { return sodio; }
    public void setSodio(double sodio) { this.sodio = sodio; }

    public String getTipoComida() { return tipoComida; }
    public void setTipoComida(String tipoComida) { this.tipoComida = tipoComida; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getDateKey() { return dateKey; }
    public void setDateKey(int dateKey) { this.dateKey = dateKey; }

    public double getEstimatedCostSnapshot() { return estimatedCostSnapshot; }
    public void setEstimatedCostSnapshot(double estimatedCostSnapshot) { this.estimatedCostSnapshot = estimatedCostSnapshot; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public boolean isFromVoiceInput() { return fromVoiceInput; }
    public void setFromVoiceInput(boolean fromVoiceInput) { this.fromVoiceInput = fromVoiceInput; }

    public boolean isFromAiFoodScan() { return fromAiFoodScan; }
    public void setFromAiFoodScan(boolean fromAiFoodScan) { this.fromAiFoodScan = fromAiFoodScan; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

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
        dest.writeString(id);
        dest.writeString(foodName);
        dest.writeString(foodId);
        dest.writeString(foodProductId);
        dest.writeDouble(cantidad);
        dest.writeInt(calorias);
        dest.writeDouble(proteinas);
        dest.writeDouble(carbohidratos);
        dest.writeDouble(grasas);
        dest.writeDouble(fibra);
        dest.writeDouble(azucar);
        dest.writeDouble(sodio);
        dest.writeString(tipoComida);
        dest.writeLong(timestamp);
        dest.writeInt(dateKey);
        dest.writeDouble(estimatedCostSnapshot);
        dest.writeString(notes);
        dest.writeString(barcode);
        dest.writeByte((byte) (fromVoiceInput ? 1 : 0));
        dest.writeByte((byte) (fromAiFoodScan ? 1 : 0));
        dest.writeString(sourceType);
        dest.writeByte((byte) (isSynced ? 1 : 0));
        dest.writeString(syncAction);
        dest.writeString(serverId);
        dest.writeLong(lastModified);
    }
}