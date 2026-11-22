package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.EscaneoEntry;

import java.util.List;

public interface EscaneoRepository {
    void insertEscaneo(EscaneoEntry entry);
    void updateEscaneo(EscaneoEntry entry);
    void deleteEscaneo(long id);
    List<EscaneoEntry> getAllEscaneos();
    EscaneoEntry getEscaneoById(long id);
    List<EscaneoEntry> getEscaneosByType(String tipo);
}