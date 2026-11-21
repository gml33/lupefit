package com.lupefit.feature.diario.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.ArrayList;
import java.util.List;

public class DiarioHomeViewModel extends ViewModel {

    private final MutableLiveData<List<DiarioEntry>> diarioEntries = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<MacroTotals> totales = new MutableLiveData<>(new MacroTotals(0, 0, 0, 0));

    public LiveData<List<DiarioEntry>> getDiarioEntries() {
        return diarioEntries;
    }

    public LiveData<MacroTotals> getTotales() {
        return totales;
    }

    public void loadDiarioDelDia() {
        // TODO: Load from Repository
        // For now, set mock data
        totales.setValue(new MacroTotals(1850, 120, 180, 75));
    }

    public void addEntry(DiarioEntry entry) {
        List<DiarioEntry> current = new ArrayList<>(diarioEntries.getValue());
        current.add(0, entry);
        diarioEntries.setValue(current);
        recalculateTotals(current);
    }

    private void recalculateTotals(List<DiarioEntry> entries) {
        int cal = 0, prot = 0, carb = 0, fat = 0;
        for (DiarioEntry e : entries) {
            cal += e.getCalorias();
            prot += e.getProteinas();
            carb += e.getCarbohidratos();
            fat += e.getGrasas();
        }
        totales.setValue(new MacroTotals(cal, prot, carb, fat));
    }
}