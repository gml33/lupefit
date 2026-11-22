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
import com.lupefit.feature.insights.databinding.FragmentPatronesBinding
import com.lupefit.feature.insights.databinding.ItemPatronAlimentarioBinding
import com.lupefit.feature.insights.viewmodel.PatronesViewModel

class PatronesFragment : Fragment() {

    private var _binding: FragmentPatronesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PatronesViewModel
    private lateinit var navController: NavController
    private lateinit var adapter: PatronesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatronesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this, PatronesViewModel.Factory(requireContext())).get(PatronesViewModel::class.java)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.toolbar.title = getString(R.string.patrones)
    }

    private fun setupRecyclerView() {
        adapter = PatronesAdapter()
        binding.rvPatrones.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPatrones.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnRecargar.setOnClickListener { viewModel.recargar() }
    }

    private fun setupObservers() {
        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando == true) View.VISIBLE else View.GONE
            binding.btnRecargar.isEnabled = cargando != true
        }

        viewModel.patrones.observe(viewLifecycleOwner) { patrones ->
            patrones?.let { adapter.submitList(it.toMutableList()) }
        }

        viewModel.patronPrincipal.observe(viewLifecycleOwner) { principal ->
            principal?.let { binding.tvPatronPrincipal.text = it }
        }

        viewModel.scoreConsistencia.observe(viewLifecycleOwner) { score ->
            score?.let {
                binding.progressConsistencia.progress = it
                binding.tvScoreConsistencia.text = "$it%"
            }
        }
    }

    private class PatronesAdapter : androidx.recyclerview.widget.ListAdapter<PatronesViewModel.PatronAlimentario, PatronesAdapter.PatronViewHolder>(
        object : androidx.recyclerview.widget.DiffUtil.ItemCallback<PatronesViewModel.PatronAlimentario>() {
            override fun areItemsTheSame(oldItem: PatronesViewModel.PatronAlimentario, newItem: PatronesViewModel.PatronAlimentario): Boolean =
                oldItem.nombre == newItem.nombre

            override fun areContentsTheSame(oldItem: PatronesViewModel.PatronAlimentario, newItem: PatronesViewModel.PatronAlimentario): Boolean =
                oldItem.frecuencia == newItem.frecuencia && oldItem.esPositivo == newItem.esPositivo
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatronViewHolder {
            val itemBinding = ItemPatronAlimentarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PatronViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: PatronViewHolder, position: Int) {
            val patron = getItem(position)
            holder.bind(patron)
        }

        class PatronViewHolder(private val binding: ItemPatronAlimentarioBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
            fun bind(patron: PatronesViewModel.PatronAlimentario) {
                binding.tvNombrePatron.text = patron.nombre
                binding.tvFrecuencia.text = "${patron.frecuencia}%"
                binding.tvDescripcionPatron.text = patron.descripcion
                binding.progressFrecuencia.progress = patron.frecuencia

                val bgColor = if (patron.esPositivo)
                    binding.root.context.getColor(R.color.status_success)
                else
                    binding.root.context.getColor(R.color.status_error)
                binding.cardPatron.setCardBackgroundColor(bgColor)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}