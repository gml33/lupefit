package com.lupefit.feature.planes.ui;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.core.domain.model.Receta;
import com.lupefit.core.domain.provider.RecetaRepositoryProvider;
import com.lupefit.feature.planes.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

final class ListaComprasDialog {
    private ListaComprasDialog() {}

    static void mostrar(Fragment fragmento, List<String> entradas) {
        Activity activity = fragmento.requireActivity();
        Lifecycle lifecycle = fragmento.getViewLifecycleOwner().getLifecycle();
        Context application = activity.getApplicationContext();
        new Thread(() -> {
            List<Receta> recetas = application instanceof RecetaRepositoryProvider
                    ? ((RecetaRepositoryProvider) application).getRecetaRepository().getAllRecetas()
                    : Collections.emptyList();
            List<String> items = consolidar(application, recetas, entradas);
            activity.runOnUiThread(() -> {
                if (lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(fragmento.requireContext())
                            .setTitle(R.string.planes_lista_compras)
                            .setPositiveButton(R.string.planes_cerrar, null);
                    if (items.isEmpty()) {
                        dialog.setMessage(R.string.planes_lista_vacia);
                    } else {
                        dialog.setItems(items.toArray(new String[0]), null);
                    }
                    dialog.show();
                }
            });
        }, "lista-compras").start();
    }

    private static List<String> consolidar(Context context, List<Receta> recetas, List<String> entradas) {
        Map<String, Receta> catalogo = new HashMap<>();
        if (recetas != null) {
            for (Receta receta : recetas) {
                if (receta.getNombre() != null) {
                    catalogo.putIfAbsent(receta.getNombre().toLowerCase(Locale.ROOT), receta);
                }
            }
        }
        Map<Ingrediente, Double> totales = new LinkedHashMap<>();
        Set<String> sinIngredientes = new LinkedHashSet<>();
        for (String entrada : entradas) {
            Receta receta = catalogo.get(entrada.toLowerCase(Locale.ROOT));
            if (receta == null || receta.getIngredientes() == null || receta.getIngredientes().isEmpty()) {
                sinIngredientes.add(entrada);
                continue;
            }
            for (IngredienteReceta ingrediente : receta.getIngredientes()) {
                if (ingrediente.getAlimentoNombre() != null && ingrediente.getCantidad() > 0) {
                    String unidad = ingrediente.getUnidad() == null ? "" : ingrediente.getUnidad();
                    Ingrediente clave = new Ingrediente(ingrediente.getAlimentoNombre(), unidad);
                    totales.merge(clave, (double) ingrediente.getCantidad(), Double::sum);
                }
            }
        }
        List<String> items = new ArrayList<>();
        NumberFormat format = NumberFormat.getNumberInstance();
        totales.forEach((ingrediente, cantidad) -> items.add(
                context.getString(R.string.planes_ingrediente_formato, ingrediente.nombre,
                        format.format(cantidad), ingrediente.unidad).trim()));
        for (String entrada : sinIngredientes) {
            items.add(context.getString(R.string.planes_receta_sin_ingredientes, entrada));
        }
        return items;
    }

    private static final class Ingrediente {
        final String nombre;
        final String unidad;

        Ingrediente(String nombre, String unidad) {
            this.nombre = nombre;
            this.unidad = unidad;
        }

        @Override public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Ingrediente)) return false;
            Ingrediente that = (Ingrediente) other;
            return nombre.equals(that.nombre) && unidad.equals(that.unidad);
        }

        @Override public int hashCode() {
            return 31 * nombre.hashCode() + unidad.hashCode();
        }
    }
}
