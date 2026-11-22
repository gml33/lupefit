package com.lupefit.app.data.local.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.lupefit.app.data.local.AppDatabase;
import com.lupefit.app.data.local.dao.ProgresoDao;
import com.lupefit.app.data.local.entity.PesoEntity;
import com.lupefit.core.domain.model.PesoEntry;
import com.lupefit.core.domain.repository.ProgresoRepository;

import java.util.ArrayList;
import java.util.List;

public class ProgresoRepositoryImpl implements ProgresoRepository {

    private final ProgresoDao progresoDao;

    public ProgresoRepositoryImpl(Context context) {
        this.progresoDao = AppDatabase.getInstance(context).progresoDao();
    }

    @Override
    public void insertPeso(PesoEntry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            PesoEntity entity = convertToEntity(entry);
            long id = progresoDao.insertPeso(entity);
            entry.setId(id);
        });
    }

    @Override
    public void getPesoHistory(OnPesoHistoryListener listener) {
        LiveData<List<PesoEntity>> liveData = progresoDao.getAllPesoEntries();
        listener.onResult(convertToDomainList(liveData.getValue()));
    }

    private List<PesoEntry> convertToDomainList(List<PesoEntity> entities) {
        List<PesoEntry> result = new ArrayList<>();
        if (entities != null) {
            for (PesoEntity entity : entities) {
                result.add(convertToDomain(entity));
            }
        }
        return result;
    }

    private PesoEntry convertToDomain(PesoEntity entity) {
        PesoEntry domain = new PesoEntry();
        domain.setId(entity.id);
        domain.setPeso(entity.peso);
        domain.setFecha(entity.fecha);
        domain.setNota(entity.nota);
        domain.setDateKey(entity.dateKey);
        domain.setTimestamp(entity.timestamp);
        domain.setDiferencia(entity.diferencia);
        return domain;
    }

    private PesoEntity convertToEntity(PesoEntry domain) {
        PesoEntity entity = new PesoEntity();
        entity.id = domain.getId();
        entity.peso = domain.getPeso();
        entity.fecha = domain.getFecha();
        entity.nota = domain.getNota();
        entity.dateKey = domain.getDateKey();
        entity.timestamp = domain.getTimestamp();
        entity.diferencia = domain.getDiferencia();
        return entity;
    }
}