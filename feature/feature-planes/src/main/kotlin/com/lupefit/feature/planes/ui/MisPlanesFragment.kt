package com.lupefit.feature.planes.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.lupefit.feature.planes.R
import com.lupefit.feature.planes.databinding.FragmentMisPlanesBinding
import com.lupefit.feature.planes.databinding.ItemPlanGuardadoBinding
import com.lupefit.feature.planes.viewmodel.PlanesComidaViewModel
import com.lupefit.feature.planes.viewmodel.PlanesEntrenamientoViewModel

class MisPlanesFragment : Fragment(R.layout.fragment_mis_planes) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMisPlanesBinding.bind(view)
        val navigation = NavHostFragment.findNavController(this)
        binding.toolbar.setNavigationOnClickListener { navigation.navigateUp() }

        val comidas = ViewModelProvider(requireActivity(), PlanesComidaViewModel.Factory(requireContext()))[PlanesComidaViewModel::class.java]
        val entrenamientos = ViewModelProvider(requireActivity(), PlanesEntrenamientoViewModel.Factory(requireContext()))[PlanesEntrenamientoViewModel::class.java]
        val semanasComida = comidas.semanas.value.orEmpty()
        val semanasEntrenamiento = entrenamientos.semanas.value.orEmpty()
        if (semanasComida.isEmpty() && semanasEntrenamiento.isEmpty()) {
            val estado = TextView(requireContext())
            estado.setTextAppearance(com.lupefit.core.ui.R.style.TextAppearance_LuPeFit_BodyMedium)
            estado.setText(R.string.planes_guardados_vacio)
            binding.contenedorPlanes.addView(estado)
        }
        for (semana in semanasComida) {
            agregarFila(binding, R.string.plan_comida, R.drawable.ic_restaurant, semana.numeroSemana) {
                comidas.setSemanaActual(semana.numeroSemana)
                navigation.navigate(R.id.planComidaFragment)
            }
        }
        for (semana in semanasEntrenamiento) {
            agregarFila(binding, R.string.plan_entrenamiento, R.drawable.ic_fitness, semana.numeroSemana) {
                entrenamientos.setSemanaActual(semana.numeroSemana)
                navigation.navigate(R.id.planEntrenamientoFragment)
            }
        }
    }

    private fun agregarFila(binding: FragmentMisPlanesBinding, titulo: Int, icono: Int, semana: Int, abrir: () -> Unit) {
        val fila = ItemPlanGuardadoBinding.inflate(layoutInflater, binding.contenedorPlanes, true)
        fila.tvPlan.text = getString(R.string.planes_guardado_formato, getString(titulo), semana)
        fila.ivTipoPlan.setImageResource(icono)
        fila.root.setOnClickListener { abrir() }
    }
}
