package com.lupefit.app.data.local.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.EscaneoDao;
import com.lupefit.app.data.local.entity.EscaneoEntity;
import com.lupefit.core.domain.model.EscaneoEntry;
import com.lupefit.core.domain.repository.EscaneoRepository;

import java.util.ArrayList;
import java.util.List;

public class EscaneoRepositoryImpl implements EscaneoRepository {

    private final EscaneoDao escaneoDao;

    public EscaneoRepositoryImpl(Context context) {
        this.escaneoDao = AppDatabase.getInstance(context).escaneoDao();
    }

    @Override
    public void insertEscaneo(EscaneoEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            EscaneoEntity entity = convertToEntity(entry);
            long id = escaneoDao.insertEscaneo(entity);
            entry.setId(id);
        });
    }

    @Override
    public void updateEscaneo(EscaneoEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            EscaneoEntity entity = convertToEntity(entry);
            escaneoDao.updateEscaneo(entity);
        });
    }

    @Override
    public void deleteEscaneo(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            escaneoDao.deleteEscaneoById(id);
        });
    }

    @Override
    public List<EscaneoEntry> getAllEscaneos() {
        LiveData<List<EscaneoEntity>> liveData = escaneoDao.getAllEscaneos();
        return convertToDomainList(liveData.getValue());
    }

    @Override
    public EscaneoEntry getEscaneoById(long id) {
        LiveData<EscaneoEntity> liveData = escaneoDao.getEscaneoById(id);
        EscaneoEntity entity = liveData.getValue();
        return entity != null ? convertToDomain(entity) : null;
    }

    @Override
    public List<EscaneoEntry> getEscaneosByType(String tipo) {
        LiveData<List<EscaneoEntity>> liveData = escaneoDao.getEscaneosByType(tipo);
        return convertToDomainList(liveData.getValue());
    }

    private List<EscaneoEntry> convertToDomainList(List<EscaneoEntity> entities) {
        List<EscaneoEntry> result = new ArrayList<>();
        if (entities != null) {
            for (EscaneoEntity entity : entities) {
                result.add(convertToDomain(entity));
            }
        }
        return result;
    }

    private EscaneoEntry convertToDomain(EscaneoEntity entity) {
        EscaneoEntry domain = new EscaneoEntry();
        domain.setId(entity.id);
        domain.setTipo(entity.tipo);
        domain.setProductoNombre(entity.productoNombre);
        domain.setMarca(entity.marca);
        domain.setCodigoBarras(entity.codigoBarras);
        domain.setFotoUri(entity.fotoUri);
        domain.setFecha(entity.fecha);
        domain.setTimestamp(entity.timestamp);
        domain.setAgregadoAlDiario(entity.agregadoAlDiario);
        domain.setDiarioEntryId(entity.diarioEntryId);
        domain.setCaloriasPor100g(entity.caloriasPor100g);
        domain.setProteinasPor100g(entity.proteinasPor100g);
        domain.setCarbohidratosPor100g(entity.carbohidratosPor100g);
        domain.setGrasasPor100g(entity.grasasPor100g);
        domain.setAlimentosDetectadosJson(entity.alimentosDetectadosJson);
        domain.setSynced(entity.isSynced);
        domain.setSyncAction(entity.syncAction);
        domain.setServerId(entity.serverId);
        domain.setLastModified(entity.lastModified);
        return domain;
    }

    private EscaneoEntity convertToEntity(EscaneoEntry domain) {
        EscaneoEntity entity = new EscaneoEntity();
        entity.id = domain.getId();
        entity.tipo = domain.getTipo();
        entity.productoNombre = domain.getProductoNombre();
        entity.marca = domain.getMarca();
        entity.codigoBarras = domain.getCodigoBarras();
        entity.fotoUri = domain.getFotoUri();
        entity.fecha = domain.getFecha();
        entity.timestamp = domain.getTimestamp();
        entity.agregadoAlDiario = domain.isAgregadoAlDiario();
        entity.diarioEntryId = domain.getDiarioEntryId();
        entity.caloriasPor100g = domain.getCaloriasPor100g();
        entity.proteinasPor100g = domain.getProteinasPor100g();
        entity.carbohidratosPor100g = domain.getCarbohidratosPor100g();
        entity.grasasPor100g = domain.getGrasasPor100g();
        entity.alimentosDetectadosJson = domain.getAlimentosDetectadosJson();
        entity.isSynced = domain.isSynced();
        entity.syncAction = domain.getSyncAction();
        entity.serverId = domain.getServerId();
        entity.lastModified = domain.getLastModified();
        return entity;
    }
}