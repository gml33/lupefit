package com.lupefit.feature.planes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import android.widget.ArrayAdapter
import android.net.Uri
import androidx.navigation.NavDeepLinkRequest
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lupefit.feature.planes.R
import com.lupefit.feature.planes.viewmodel.PlanesComidaViewModel

class PlanComidaFragment : Fragment() {

    private lateinit var viewModel: PlanesComidaViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plan_comida, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(requireActivity(), PlanesComidaViewModel.Factory(requireContext()))[PlanesComidaViewModel::class.java]

        setupToolbar(view)
        setupRecyclerView(view)
        setupSelectorSemana(view)
        setupFab(view)
        setupObservers()
        view.findViewById<View>(R.id.btnListaCompras).setOnClickListener {
            val compras = viewModel.listaCompras.value.orEmpty()
            ListaComprasDialog.mostrar(this, compras)
        }
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { navController.navigateUp() }
        toolbar.title = getString(R.string.plan_comida)
    }

    private fun setupSelectorSemana(view: View) {
        val selector = view.findViewById<MaterialAutoCompleteTextView>(R.id.selectorSemana)
        val semanas = (1..52).map { getString(R.string.planes_semana_numero, it) }
        selector.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, semanas))
        selector.setOnItemClickListener { _, _, position, _ -> viewModel.setSemanaActual(position + 1) }
        viewModel.semanaActual.observe(viewLifecycleOwner) { semana ->
            selector.setText(semanas[semana - 1], false)
            renderDias()
        }
    }

    private fun setupFab(view: View) {
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCrearPlan)
            .setOnClickListener {
                navController.navigate(R.id.action_planComidaFragment_to_crearPlanComidaFragment)
            }
    }

    private fun setupRecyclerView(view: View) {
        val rvDias = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvDias)
        rvDias.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        rvDias.adapter = DiasComidaAdapter({ dia ->
            viewModel.toggleDiaCompletado(viewModel.semanaActual.value ?: 1, dia)
        }, {
            navController.navigate(NavDeepLinkRequest.Builder.fromUri(Uri.parse("lupefit://diario/quick-add")).build())
        })
    }

    private fun setupObservers() {
        viewModel.semanas.observe(viewLifecycleOwner) { renderDias() }
    }

    private fun renderDias() {
        val semana = viewModel.semanaActual.value ?: 1
        val dias = viewModel.semanas.value.orEmpty().find { it.numeroSemana == semana }?.dias.orEmpty()
        (requireView().findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvDias).adapter as DiasComidaAdapter)
            .submitList(dias)
        requireView().findViewById<View>(R.id.tvSemanaSinPlan).visibility = if (dias.isEmpty()) View.VISIBLE else View.GONE
    }

    private class DiasComidaAdapter(private val onToggle: (Int) -> Unit, private val onRegistrar: () -> Unit) : androidx.recyclerview.widget.ListAdapter<PlanesComidaViewModel.DiaPlan, DiasComidaAdapter.DiaViewHolder>(
        object : androidx.recyclerview.widget.DiffUtil.ItemCallback<PlanesComidaViewModel.DiaPlan>() {
            override fun areItemsTheSame(oldItem: PlanesComidaViewModel.DiaPlan, newItem: PlanesComidaViewModel.DiaPlan): Boolean = oldItem.dia == newItem.dia
            override fun areContentsTheSame(oldItem: PlanesComidaViewModel.DiaPlan, newItem: PlanesComidaViewModel.DiaPlan): Boolean = oldItem == newItem
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia_plan, parent, false)
            return DiaViewHolder(view)
        }

        override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
            holder.bind(getItem(position), onToggle, onRegistrar)
        }

        class DiaViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            private val tvDia: android.widget.TextView = view.findViewById(R.id.tvDia)
            private val tvRecetas: android.widget.TextView = view.findViewById(R.id.tvRecetas)
            private val cbCompletado: android.widget.CheckBox = view.findViewById(R.id.cbCompletado)

            fun bind(dia: PlanesComidaViewModel.DiaPlan, onToggle: (Int) -> Unit, onRegistrar: () -> Unit) {
                tvDia.text = dia.nombre
                tvRecetas.text = dia.recetas.joinToString(", ")
                cbCompletado.isEnabled = dia.recetas.isNotEmpty()
                cbCompletado.setOnCheckedChangeListener(null)
                cbCompletado.isChecked = dia.completado
                cbCompletado.setOnCheckedChangeListener { _, _ -> onToggle(dia.dia) }
                itemView.findViewById<View>(R.id.btnRegistrar).setOnClickListener { onRegistrar() }
            }
        }
    }
}
