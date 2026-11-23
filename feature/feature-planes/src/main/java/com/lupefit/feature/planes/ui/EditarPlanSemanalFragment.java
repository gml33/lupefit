package com.lupefit.feature.planes.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.lupefit.feature.planes.R;
import com.lupefit.feature.planes.databinding.FragmentEditarPlanSemanalBinding;
import com.lupefit.feature.planes.databinding.ItemCampoPlanSemanalBinding;
import com.lupefit.feature.planes.viewmodel.PlanesComidaViewModel;
import com.lupefit.feature.planes.viewmodel.PlanesEntrenamientoViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EditarPlanSemanalFragment extends Fragment {
    private static final String STATE_DRAFT = "borrador";
    private final List<TextInputEditText> campos = new ArrayList<>();

    public EditarPlanSemanalFragment() {
        super(R.layout.fragment_editar_plan_semanal);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FragmentEditarPlanSemanalBinding binding = FragmentEditarPlanSemanalBinding.bind(view);
        NavController navigation = NavHostFragment.findNavController(this);
        boolean entrenamiento = requireArguments().getBoolean("entrenamiento");
        binding.toolbar.setTitle(entrenamiento ? R.string.crear_plan_entrenamiento : R.string.crear_plan_comida);
        binding.toolbar.setNavigationOnClickListener(unused -> navigation.navigateUp());
        binding.tvInstrucciones.setText(entrenamiento
                ? R.string.planes_entradas_entrenamiento : R.string.planes_entradas_comida);

        PlanesComidaViewModel comida = new ViewModelProvider(requireActivity(),
                new PlanesComidaViewModel.Factory(requireContext())).get(PlanesComidaViewModel.class);
        PlanesEntrenamientoViewModel ejercicio = new ViewModelProvider(requireActivity(),
                new PlanesEntrenamientoViewModel.Factory(requireContext())).get(PlanesEntrenamientoViewModel.class);
        Integer semanaActiva = entrenamiento ? ejercicio.getSemanaActual().getValue() : comida.getSemanaActual().getValue();
        int semana = semanaActiva == null ? 1 : semanaActiva;
        binding.tvSemana.setText(getString(R.string.planes_semana_numero, semana));

        ArrayList<String> borrador = savedInstanceState == null
                ? null : savedInstanceState.getStringArrayList(STATE_DRAFT);
        List<String> valores = borrador == null ? cargarValores(comida, ejercicio, entrenamiento, semana) : borrador;
        List<String> dias = Arrays.asList(getResources().getStringArray(R.array.planes_dias));

        for (int i = 0; i < dias.size(); i++) {
            ItemCampoPlanSemanalBinding campo = ItemCampoPlanSemanalBinding.inflate(
                    LayoutInflater.from(requireContext()), binding.contenedorDias, true);
            campo.getRoot().setHint(dias.get(i));
            campo.etEntradas.setSaveEnabled(false);
            if (i < valores.size()) {
                campo.etEntradas.setText(valores.get(i));
            }
            campos.add(campo.etEntradas);
        }

        binding.btnGuardarPlan.setOnClickListener(unused -> {
            List<List<String>> entradas = new ArrayList<>();
            boolean tieneEntradas = false;
            for (String valor : leerCampos()) {
                List<String> entradasDia = new ArrayList<>();
                for (String linea : valor.split("\n")) {
                    String entrada = linea.trim();
                    if (!entrada.isEmpty()) {
                        entradasDia.add(entrada);
                    }
                }
                tieneEntradas |= !entradasDia.isEmpty();
                entradas.add(entradasDia);
            }
            if (!tieneEntradas) {
                Toast.makeText(requireContext(), R.string.planes_entradas_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (entrenamiento) {
                ejercicio.guardarSemana(semana, dias, entradas);
            } else {
                comida.guardarSemana(semana, dias, entradas);
            }
            navigation.navigateUp();
        });
    }

    private static List<String> cargarValores(PlanesComidaViewModel comida,
            PlanesEntrenamientoViewModel ejercicio, boolean entrenamiento, int semana) {
        List<String> resultado = new ArrayList<>();
        if (entrenamiento) {
            List<PlanesEntrenamientoViewModel.SemanaEntrenamiento> semanas = ejercicio.getSemanas().getValue();
            if (semanas != null) {
                for (PlanesEntrenamientoViewModel.SemanaEntrenamiento plan : semanas) {
                    if (plan.getNumeroSemana() == semana) {
                        for (PlanesEntrenamientoViewModel.DiaEntrenamiento dia : plan.getDias()) {
                            resultado.add(String.join("\n", dia.getRutinasAsignadas()));
                        }
                    }
                }
            }
        } else {
            List<PlanesComidaViewModel.SemanaPlan> semanas = comida.getSemanas().getValue();
            if (semanas != null) {
                for (PlanesComidaViewModel.SemanaPlan plan : semanas) {
                    if (plan.getNumeroSemana() == semana) {
                        for (PlanesComidaViewModel.DiaPlan dia : plan.getDias()) {
                            resultado.add(String.join("\n", dia.getRecetas()));
                        }
                    }
                }
            }
        }
        return resultado;
    }

    private ArrayList<String> leerCampos() {
        ArrayList<String> texto = new ArrayList<>();
        for (TextInputEditText campo : campos) {
            texto.add(campo.getText() == null ? "" : campo.getText().toString());
        }
        return texto;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATE_DRAFT, leerCampos());
    }

    @Override
    public void onDestroyView() {
        campos.clear();
        super.onDestroyView();
    }
}
