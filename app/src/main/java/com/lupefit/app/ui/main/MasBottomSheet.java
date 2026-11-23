package com.lupefit.app.ui.main;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupefit.app.R;

public class MasBottomSheet extends AppCompatDialogFragment {

    static final int[] DESTINATIONS = {
            R.id.nav_recetas, R.id.nav_escaneo, R.id.nav_agua, R.id.nav_configuracion,
            R.id.nav_perfil, R.id.nav_planes
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] options = {
                getText(R.string.recetas_title), getText(R.string.escanear_codigo_barras),
                getText(R.string.agua_title), getText(R.string.configuracion_title),
                getText(R.string.perfil_title),
                getText(R.string.planes_title)
        };
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.mas)
                .setItems(options, (dialog, index) ->
                        NavHostFragment.findNavController(this).navigate(DESTINATIONS[index]))
                .create();
    }
}
