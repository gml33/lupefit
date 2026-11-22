package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Receta implements Parcelable {

    private long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private int tiempoPrepMin;
    private int tiempoCoccionMin;
    private int porciones;
    private String dificultad;
    private String instrucciones;
    private String fotoUri;
    private List<IngredienteReceta> ingredientes;
    private boolean esFavorita;
    private boolean esPublica;
    private long userId;

    // Calculated nutrition (per serving)
    private int caloriasPorPorcion;
    private float proteinasPorPorcion;
    private float carbohidratosPorPorcion;
    private float grasasPorPorcion;
    private float fibraPorPorcion;

    // Sync fields
    private boolean isSynced = false;
    private String syncAction = "CREATE";
    private String serverId;
    private long timestamp = System.currentTimeMillis();
    private long lastModified = System.currentTimeMillis();

    public Receta() {
        this.ingredientes = new ArrayList<>();
    }

    protected Receta(Parcel in) {
        id = in.readLong();
        nombre = in.readString();
        descripcion = in.readString();
        categoria = in.readString();
        tiempoPrepMin = in.readInt();
        tiempoCoccionMin = in.readInt();
        porciones = in.readInt();
        dificultad = in.readString();
        instrucciones = in.readString();
        fotoUri = in.readString();
        ingredientes = in.createTypedArrayList(IngredienteReceta.CREATOR);
        esFavorita = in.readByte() != 0;
        esPublica = in.readByte() != 0;
        userId = in.readLong();
        caloriasPorPorcion = in.readInt();
        proteinasPorPorcion = in.readFloat();
        carbohidratosPorPorcion = in.readFloat();
        grasasPorPorcion = in.readFloat();
        fibraPorPorcion = in.readFloat();
        isSynced = in.readByte() != 0;
        syncAction = in.readString();
        serverId = in.readString();
        timestamp = in.readLong();
        lastModified = in.readLong();
    }

    public static final Creator<Receta> CREATOR = new Creator<Receta>() {
        @Override
        public Receta createFromParcel(Parcel in) {
            return new Receta(in);
        }

        @Override
        public Receta[] newArray(int size) {
            return new Receta[size];
        }
    };

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getTiempoPrepMin() { return tiempoPrepMin; }
    public void setTiempoPrepMin(int tiempoPrepMin) { this.tiempoPrepMin = tiempoPrepMin; }

    public int getTiempoCoccionMin() { return tiempoCoccionMin; }
    public void setTiempoCoccionMin(int tiempoCoccionMin) { this.tiempoCoccionMin = tiempoCoccionMin; }

    public int getPorciones() { return porciones; }
    public void setPorciones(int porciones) { this.porciones = porciones; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public String getFotoUri() { return fotoUri; }
    public void setFotoUri(String fotoUri) { this.fotoUri = fotoUri; }

    public List<IngredienteReceta> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<IngredienteReceta> ingredientes) { this.ingredientes = ingredientes; }

    public boolean isEsFavorita() { return esFavorita; }
    public void setEsFavorita(boolean esFavorita) { this.esFavorita = esFavorita; }

    public boolean isEsPublica() { return esPublica; }
    public void setEsPublica(boolean esPublica) { this.esPublica = esPublica; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public int getCaloriasPorPorcion() { return caloriasPorPorcion; }
    public void setCaloriasPorPorcion(int caloriasPorPorcion) { this.caloriasPorPorcion = caloriasPorPorcion; }

    public float getProteinasPorPorcion() { return proteinasPorPorcion; }
    public void setProteinasPorPorcion(float proteinasPorPorcion) { this.proteinasPorPorcion = proteinasPorPorcion; }

    public float getCarbohidratosPorPorcion() { return carbohidratosPorPorcion; }
    public void setCarbohidratosPorPorcion(float carbohidratosPorPorcion) { this.carbohidratosPorPorcion = carbohidratosPorPorcion; }

    public float getGrasasPorPorcion() { return grasasPorPorcion; }
    public void setGrasasPorPorcion(float grasasPorPorcion) { this.grasasPorPorcion = grasasPorPorcion; }

    public float getFibraPorPorcion() { return fibraPorPorcion; }
    public void setFibraPorPorcion(float fibraPorPorcion) { this.fibraPorPorcion = fibraPorPorcion; }

    public boolean isSynced() { return isSynced; }
    public void setSynced(boolean synced) { isSynced = synced; }

    public String getSyncAction() { return syncAction; }
    public void setSyncAction(String syncAction) { this.syncAction = syncAction; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

    // Helper methods
    public int getTiempoTotalMin() {
        return tiempoPrepMin + tiempoCoccionMin;
    }

    public void addIngrediente(IngredienteReceta ingrediente) {
        if (ingredientes == null) {
            ingredientes = new ArrayList<>();
        }
        ingredientes.add(ingrediente);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(categoria);
        dest.writeInt(tiempoPrepMin);
        dest.writeInt(tiempoCoccionMin);
        dest.writeInt(porciones);
        dest.writeString(dificultad);
        dest.writeString(instrucciones);
        dest.writeString(fotoUri);
        dest.writeTypedList(ingredientes);
        dest.writeByte((byte) (esFavorita ? 1 : 0));
        dest.writeByte((byte) (esPublica ? 1 : 0));
        dest.writeLong(userId);
        dest.writeInt(caloriasPorPorcion);
        dest.writeFloat(proteinasPorPorcion);
        dest.writeFloat(carbohidratosPorPorcion);
        dest.writeFloat(grasasPorPorcion);
        dest.writeFloat(fibraPorPorcion);
        dest.writeByte((byte) (isSynced ? 1 : 0));
        dest.writeString(syncAction);
        dest.writeString(serverId);
        dest.writeLong(timestamp);
        dest.writeLong(lastModified);
    }
}