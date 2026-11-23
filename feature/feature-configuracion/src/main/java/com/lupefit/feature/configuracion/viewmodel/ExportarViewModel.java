package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.provider.ProgresoRepositoryProvider;
import com.lupefit.core.domain.provider.RecetaRepositoryProvider;
import com.lupefit.core.domain.provider.RoutineRepositoryProvider;
import com.lupefit.core.domain.provider.WaterRepositoryProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExportarViewModel extends ViewModel {
    public enum FormatoExportacion { CSV, JSON }
    public enum TipoDato { TODO, DIARIO, EJERCICIO, PROGRESO, RECETAS, AGUA }

    private final Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<FormatoExportacion> _formato = new MutableLiveData<>(FormatoExportacion.CSV);
    public final LiveData<FormatoExportacion> formato = _formato;
    private final MutableLiveData<TipoDato> _tipoDato = new MutableLiveData<>(TipoDato.TODO);
    public final LiveData<TipoDato> tipoDato = _tipoDato;
    private final MutableLiveData<String> _fechaInicio = new MutableLiveData<>("");
    public final LiveData<String> fechaInicio = _fechaInicio;
    private final MutableLiveData<String> _fechaFin = new MutableLiveData<>("");
    public final LiveData<String> fechaFin = _fechaFin;
    private final MutableLiveData<Boolean> _exportando = new MutableLiveData<>(false);
    public final LiveData<Boolean> exportando = _exportando;
    private final MutableLiveData<String> _progreso = new MutableLiveData<>("");
    public final LiveData<String> progreso = _progreso;
    private final MutableLiveData<String> _archivoGenerado = new MutableLiveData<>("");
    public final LiveData<String> archivoGenerado = _archivoGenerado;
    public final LiveData<Boolean> archivoGeneradoVisible = Transformations.map(_archivoGenerado,
            value -> value != null && !value.isEmpty());
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private LiveData<List<DiarioEntry>> pendingDiary;
    private Observer<List<DiarioEntry>> diaryObserver;

    private ExportarViewModel(Context context) {
        this.context = context.getApplicationContext();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == ExportarViewModel.class) {
                return (T) new ExportarViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public boolean fechasValidas() {
        LocalDate inicio = parseDate(_fechaInicio.getValue());
        LocalDate fin = parseDate(_fechaFin.getValue());
        if (inicio != null && fin != null && inicio.isAfter(fin)) {
            _error.setValue("La fecha de inicio debe ser anterior a la fecha de fin");
            return false;
        }
        return true;
    }

    public void exportar(Uri uri, FormatoExportacion format) {
        if (uri == null || Boolean.TRUE.equals(_exportando.getValue()) || !fechasValidas()) {
            return;
        }
        TipoDato type = _tipoDato.getValue();
        LocalDate inicio = parseDate(_fechaInicio.getValue());
        LocalDate fin = parseDate(_fechaFin.getValue());
        _exportando.setValue(true);
        _progreso.setValue("Preparando datos…");
        _archivoGenerado.setValue("");
        _error.setValue(null);
        if (type == TipoDato.TODO || type == TipoDato.DIARIO) {
            pendingDiary = ((DiarioRepositoryProvider) context).getDiarioRepository()
                    .getEntriesForDateRange(dateKey(inicio, 19000101), dateKey(fin, 99991231));
            diaryObserver = entries -> {
                clearDiaryObserver();
                writeFile(uri, type, format, inicio, fin, entries);
            };
            pendingDiary.observeForever(diaryObserver);
        } else {
            writeFile(uri, type, format, inicio, fin, List.of());
        }
    }

    private void writeFile(Uri uri, TipoDato type, FormatoExportacion format, LocalDate inicio,
                           LocalDate fin, List<DiarioEntry> diary) {
        executor.execute(() -> {
            try {
                List<ExportRow> rows = new ArrayList<>();
                if (diary != null) {
                    diary.forEach(row -> rows.add(new ExportRow("diario", row)));
                }
                if (type == TipoDato.TODO || type == TipoDato.EJERCICIO) {
                    ((RoutineRepositoryProvider) context).getRoutineRepository().list().stream()
                            .filter(row -> dateMatches(row.completedAt, inicio, fin))
                            .forEach(row -> rows.add(new ExportRow("rutina", row)));
                }
                if (type == TipoDato.TODO || type == TipoDato.RECETAS) {
                    ((RecetaRepositoryProvider) context).getRecetaRepository().getAllRecetas().stream()
                            .filter(row -> dateMatches(row.getTimestamp(), inicio, fin))
                            .forEach(row -> rows.add(new ExportRow("receta", row)));
                }
                if (type == TipoDato.TODO || type == TipoDato.AGUA) {
                    ((WaterRepositoryProvider) context).getWaterRepository().getAllWaterEntries().stream()
                            .filter(row -> dateMatches(row.getTimestamp(), inicio, fin))
                            .forEach(row -> rows.add(new ExportRow("agua", row)));
                }
                if (type == TipoDato.TODO || type == TipoDato.PROGRESO) {
                    ((ProgresoRepositoryProvider) context).getProgresoRepository().getPesoHistory(entries ->
                            entries.stream().filter(row -> dateMatches(row.getTimestamp(), inicio, fin))
                                    .forEach(row -> rows.add(new ExportRow("peso", row))));
                }

                Gson gson = new Gson();
                try (OutputStream output = context.getContentResolver().openOutputStream(uri, "wt")) {
                    if (output == null) {
                        throw new IOException("No se pudo abrir el archivo elegido");
                    }
                    try (OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)) {
                        if (format == FormatoExportacion.JSON) {
                            writer.write(gson.toJson(rows));
                        } else {
                            writer.write("tipo,datos\r\n");
                            for (ExportRow row : rows) {
                                writer.write(row.tipo + ",\"" + gson.toJson(row.datos).replace("\"", "\"\"") + "\"\r\n");
                            }
                        }
                    }
                }
                _archivoGenerado.postValue("Archivo guardado · " + rows.size() + " registros");
            } catch (IOException | RuntimeException failure) {
                _error.postValue("No se pudo exportar el archivo");
            } finally {
                _exportando.postValue(false);
            }
        });
    }

    private static LocalDate parseDate(String value) {
        return value == null || value.isEmpty() ? null : LocalDate.parse(value);
    }

    private static int dateKey(LocalDate date, int fallback) {
        return date == null ? fallback : Integer.parseInt(date.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    private static boolean dateMatches(long timestamp, LocalDate inicio, LocalDate fin) {
        if (inicio == null && fin == null) {
            return true;
        }
        if (timestamp <= 0) {
            return false;
        }
        LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        return (inicio == null || !date.isBefore(inicio)) && (fin == null || !date.isAfter(fin));
    }

    public void setFormato(FormatoExportacion formato) {
        _formato.setValue(formato);
    }

    public void setTipoDato(TipoDato tipo) {
        _tipoDato.setValue(tipo);
    }

    public void setFechaInicio(String fecha) {
        _fechaInicio.setValue(fecha);
    }

    public void setFechaFin(String fecha) {
        _fechaFin.setValue(fecha);
    }

    private void clearDiaryObserver() {
        if (pendingDiary != null) {
            pendingDiary.removeObserver(diaryObserver);
            pendingDiary = null;
            diaryObserver = null;
        }
    }

    @Override
    protected void onCleared() {
        clearDiaryObserver();
        executor.shutdown();
    }

    private static final class ExportRow {
        final String tipo;
        final Object datos;

        ExportRow(String tipo, Object datos) {
            this.tipo = tipo;
            this.datos = datos;
        }
    }
}
