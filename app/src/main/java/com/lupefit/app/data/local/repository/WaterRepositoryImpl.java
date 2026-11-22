package com.lupefit.app.data.local.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.WaterDao;
import com.lupefit.app.data.local.entity.WaterEntity;
import com.lupefit.core.domain.model.WaterEntry;
import com.lupefit.core.domain.repository.WaterRepository;

import java.util.ArrayList;
import java.util.List;

public class WaterRepositoryImpl implements WaterRepository {

    private final WaterDao waterDao;

    public WaterRepositoryImpl(Context context) {
        this.waterDao = AppDatabase.getInstance(context).waterDao();
    }

    @Override
    public void insertWater(WaterEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            WaterEntity entity = convertToEntity(entry);
            long id = waterDao.insertWater(entity);
            entry.setId(id);
        });
    }

    @Override
    public void updateWater(WaterEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            WaterEntity entity = convertToEntity(entry);
            waterDao.updateWater(entity);
        });
    }

    @Override
    public void deleteWater(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            waterDao.deleteWaterById(id);
        });
    }

    @Override
    public List<WaterEntry> getAllWaterEntries() {
        LiveData<List<WaterEntity>> liveData = waterDao.getAllWaterEntries();
        return convertToDomainList(liveData.getValue());
    }

    @Override
    public List<WaterEntry> getWaterEntriesByDate(String fecha) {
        LiveData<List<WaterEntity>> liveData = waterDao.getWaterEntriesByDate(fecha);
        return convertToDomainList(liveData.getValue());
    }

    @Override
    public WaterEntry getWaterEntryById(long id) {
        LiveData<WaterEntity> liveData = waterDao.getWaterById(id);
        WaterEntity entity = liveData.getValue();
        return entity != null ? convertToDomain(entity) : null;
    }

    @Override
    public int getTotalWaterToday(String fecha) {
        LiveData<Integer> liveData = waterDao.getTotalWaterToday(fecha);
        Integer total = liveData.getValue();
        return total != null ? total : 0;
    }

    private List<WaterEntry> convertToDomainList(List<WaterEntity> entities) {
        List<WaterEntry> result = new ArrayList<>();
        if (entities != null) {
            for (WaterEntity entity : entities) {
                result.add(convertToDomain(entity));
            }
        }
        return result;
    }

    private WaterEntry convertToDomain(WaterEntity entity) {
        WaterEntry domain = new WaterEntry();
        domain.setId(entity.id);
        domain.setCantidadMl(entity.cantidadMl);
        domain.setFecha(entity.fecha);
        domain.setHora(entity.hora);
        domain.setTimestamp(entity.timestamp);
        domain.setTipoVaso(entity.tipoVaso);
        return domain;
    }

    private WaterEntity convertToEntity(WaterEntry domain) {
        WaterEntity entity = new WaterEntity();
        entity.id = domain.getId();
        entity.cantidadMl = domain.getCantidadMl();
        entity.fecha = domain.getFecha();
        entity.hora = domain.getHora();
        entity.timestamp = domain.getTimestamp();
        entity.tipoVaso = domain.getTipoVaso();
        return entity;
    }
}