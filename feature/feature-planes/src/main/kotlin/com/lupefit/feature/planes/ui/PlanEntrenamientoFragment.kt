package com.lupefit.feature.planes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.lupefit.feature.planes.R
import com.lupefit.feature.planes.viewmodel.PlanesEntrenamientoViewModel

class PlanEntrenamientoFragment : Fragment() {

    private lateinit var viewModel: PlanesEntrenamientoViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plan_entrenamiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this).get(PlanesEntrenamientoViewModel::class.java)

        setupToolbar(view)
        setupChipGroup(view)
        setupFab(view)
        setupRecyclerView(view)
        setupObservers()
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { navController.navigateUp() }
        toolbar.title = getString(R.string.plan_entrenamiento)
    }

    private fun setupChipGroup(view: View) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupSemanas)
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val semana = when (checkedId) {
                R.id.chipSemana1 -> 1
                R.id.chipSemana2 -> 2
                R.id.chipSemana3 -> 3
                R.id.chipSemana4 -> 4
                else -> 1
            }
            viewModel.setSemanaActual(semana)
        }
        (chipGroup.getChildAt(0) as Chip).isChecked = true
    }

    private fun setupFab(view: View) {
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCrearPlan)
            .setOnClickListener {
                navController.navigate(R.id.action_planEntrenamientoFragment_to_crearPlanEntrenamientoFragment)
            }
    }

    private fun setupRecyclerView(view: View) {
        val rvDias = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvDias)
        rvDias.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        rvDias.adapter = DiasEntrenamientoAdapter()
    }

    private fun setupObservers() {
        viewModel.semanas.observe(viewLifecycleOwner) { semanas ->
            (requireView().findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvDias).adapter as? DiasEntrenamientoAdapter)
                ?.submitList(semanas?.firstOrNull { it.numeroSemana == viewModel.semanaActual.value ?: 1 }?.dias ?: emptyList())
        }
    }

    private class DiasEntrenamientoAdapter : androidx.recyclerview.widget.ListAdapter<PlanesEntrenamientoViewModel.DiaEntrenamiento, DiasEntrenamientoAdapter.DiaViewHolder>(
        object : androidx.recyclerview.widget.DiffUtil.ItemCallback<PlanesEntrenamientoViewModel.DiaEntrenamiento>() {
            override fun areItemsTheSame(oldItem: PlanesEntrenamientoViewModel.DiaEntrenamiento, newItem: PlanesEntrenamientoViewModel.DiaEntrenamiento): Boolean = oldItem.dia == newItem.dia
            override fun areContentsTheSame(oldItem: PlanesEntrenamientoViewModel.DiaEntrenamiento, newItem: PlanesEntrenamientoViewModel.DiaEntrenamiento): Boolean = oldItem == newItem
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia_plan, parent, false)
            return DiaViewHolder(view)
        }

        override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        class DiaViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            private val tvDia: android.widget.TextView = view.findViewById(R.id.tvDia)
            private val tvRutinas: android.widget.TextView = view.findViewById(R.id.tvRecetas)
            private val cbCompletado: android.widget.CheckBox = view.findViewById(R.id.cbCompletado)

            fun bind(dia: PlanesEntrenamientoViewModel.DiaEntrenamiento) {
                tvDia.text = dia.nombre
                tvRutinas.text = dia.rutinasAsignadas.joinToString(", ")
                cbCompletado.isChecked = dia.completado
                cbCompletado.setOnCheckedChangeListener { _, isChecked ->
                    // TODO: Call viewModel.toggleDiaCompletado
                }
            }
        }
    }
}