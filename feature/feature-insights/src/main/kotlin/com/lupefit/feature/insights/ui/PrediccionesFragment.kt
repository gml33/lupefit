package com.lupefit.feature.insights.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lupefit.feature.insights.R
import com.lupefit.feature.insights.databinding.FragmentPrediccionesBinding
import com.lupefit.feature.insights.viewmodel.PrediccionesViewModel

class PrediccionesFragment : Fragment() {

    private var _binding: FragmentPrediccionesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PrediccionesViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrediccionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this, PrediccionesViewModel.Factory(requireContext())).get(PrediccionesViewModel::class.java)

        setupToolbar()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.toolbar.title = getString(R.string.predicciones)
    }

    private fun setupClickListeners() {
        binding.btnRecargar.setOnClickListener { viewModel.recargar() }
    }

    private fun setupObservers() {
        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando == true) View.VISIBLE else View.GONE
            binding.btnRecargar.isEnabled = cargando != true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}