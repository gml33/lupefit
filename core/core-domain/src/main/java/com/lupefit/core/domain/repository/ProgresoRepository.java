package com.lupefit.core.domain.repository;

import com.lupefit.core.domain.model.PesoEntry;

import java.util.List;

public interface ProgresoRepository {
    final class Measurement {
        public final String kind;
        public final double centimeters;
        public final long timestamp;

        public Measurement(String kind, double centimeters, long timestamp) {
            this.kind = kind;
            this.centimeters = centimeters;
            this.timestamp = timestamp;
        }
    }

    void saveMeasurement(String kind, double centimeters);
    List<Measurement> listMeasurements();
    void insertPeso(PesoEntry entry);
    void getPesoHistory(OnPesoHistoryListener listener);

    interface OnPesoHistoryListener {
        void onResult(List<PesoEntry> entries);
    }
}