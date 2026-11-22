package com.lupefit.feature.insights.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lupefit.feature.insights.R
import com.lupefit.feature.insights.databinding.FragmentSemanalBinding
import com.lupefit.feature.insights.databinding.ItemResumenMacroBinding
import com.lupefit.feature.insights.viewmodel.SemanalViewModel

class SemanalFragment : Fragment() {

    private var _binding: FragmentSemanalBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SemanalViewModel
    private lateinit var navController: NavController
    private lateinit var adapter: MacroAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSemanalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this, SemanalViewModel.Factory(requireContext())).get(SemanalViewModel::class.java)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.toolbar.title = getString(R.string.resumen_semanal)
    }

    private fun setupRecyclerView() {
        adapter = MacroAdapter()
        binding.rvResumenSemanal.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResumenSemanal.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnRecargar.setOnClickListener { viewModel.recargar() }
    }

    private fun setupObservers() {
        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando == true) View.VISIBLE else View.GONE
            binding.btnRecargar.isEnabled = cargando != true
        }

        viewModel.macroResumenes.observe(viewLifecycleOwner) { macros ->
            macros?.let { adapter.submitList(it.toMutableList()) }
        }
    }

    private class MacroAdapter : androidx.recyclerview.widget.ListAdapter<SemanalViewModel.MacroResumen, MacroAdapter.MacroViewHolder>(
        object : androidx.recyclerview.widget.DiffUtil.ItemCallback<SemanalViewModel.MacroResumen>() {
            override fun areItemsTheSame(oldItem: SemanalViewModel.MacroResumen, newItem: SemanalViewModel.MacroResumen): Boolean =
                oldItem.nombre == newItem.nombre

            override fun areContentsTheSame(oldItem: SemanalViewModel.MacroResumen, newItem: SemanalViewModel.MacroResumen): Boolean =
                oldItem.valor == newItem.valor && oldItem.progreso == newItem.progreso
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MacroViewHolder {
            val itemBinding = ItemResumenMacroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MacroViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: MacroViewHolder, position: Int) {
            val macro = getItem(position)
            holder.bind(macro)
        }

        class MacroViewHolder(private val binding: ItemResumenMacroBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
            fun bind(macro: SemanalViewModel.MacroResumen) {
                binding.tvMacroNombre.text = macro.nombre
                binding.tvMacroValor.text = macro.valor
                binding.progressMacro.progress = macro.progreso
                binding.tvMacroPorcentaje.text = macro.porcentaje
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}