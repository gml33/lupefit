package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.PesoEntry;

import java.util.List;

public interface ProgresoRepository {
    void insertPeso(PesoEntry entry);
    void getPesoHistory(OnPesoHistoryListener listener);

    interface OnPesoHistoryListener {
        void onResult(List<PesoEntry> entries);
    }
}