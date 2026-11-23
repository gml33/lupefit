package com.lupefit.feature.planes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.lupefit.feature.planes.R
import com.lupefit.feature.planes.viewmodel.PlanesComidaViewModel
import com.lupefit.feature.planes.viewmodel.PlanesEntrenamientoViewModel
import com.lupefit.feature.planes.viewmodel.PresupuestoPlanesViewModel
import com.lupefit.feature.planes.viewmodel.PlanesHomeViewModel

class PlanesHomeFragment : Fragment() {

    private lateinit var viewModel: PlanesHomeViewModel
    private lateinit var comidaViewModel: PlanesComidaViewModel
    private lateinit var entrenamientoViewModel: PlanesEntrenamientoViewModel
    private lateinit var presupuestoViewModel: PresupuestoPlanesViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_planes_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this).get(PlanesHomeViewModel::class.java)
        comidaViewModel = ViewModelProvider(requireActivity(), PlanesComidaViewModel.Factory(requireContext()))[PlanesComidaViewModel::class.java]
        entrenamientoViewModel = ViewModelProvider(requireActivity(), PlanesEntrenamientoViewModel.Factory(requireContext()))[PlanesEntrenamientoViewModel::class.java]
        presupuestoViewModel = ViewModelProvider(requireActivity(), PresupuestoPlanesViewModel.Factory(requireContext()))[PresupuestoPlanesViewModel::class.java]

        setupToolbar(view)
        setupClickListeners(view)
        setupObservers(view)
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { navController.navigateUp() }
        toolbar.title = getString(R.string.planes_title)
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<View>(R.id.cardPlanesComida).setOnClickListener {
            navController.navigate(R.id.action_planesHomeFragment_to_planComidaFragment)
        }
        view.findViewById<View>(R.id.cardPlanesEntrenamiento).setOnClickListener {
            navController.navigate(R.id.action_planesHomeFragment_to_planEntrenamientoFragment)
        }
        view.findViewById<View>(R.id.cardPresupuesto).setOnClickListener {
            navController.navigate(R.id.action_planesHomeFragment_to_presupuestoPlanFragment)
        }
        view.findViewById<View>(R.id.cardMisPlanes).setOnClickListener {
            navController.navigate(R.id.action_planesHomeFragment_to_misPlanesFragment)
        }
    }

    private fun setupObservers(view: View) {
        comidaViewModel.semanaActual.observe(viewLifecycleOwner) { semana ->
            val tv = view.findViewById<TextView>(R.id.tvSemanaComida)
            tv?.text = getString(R.string.planes_resumen_comida, semana)
        }

        entrenamientoViewModel.progresoSemanal.observe(viewLifecycleOwner) { progreso ->
            val progress = view.findViewById<LinearProgressIndicator>(R.id.progressEntrenamiento)
            progress?.progress = progreso
            val tv = view.findViewById<TextView>(R.id.tvProgresoEntrenamiento)
            tv?.text = "$progreso%"
        }

        presupuestoViewModel.porcentajeMeta.observe(viewLifecycleOwner) { porcentaje ->
            val progress = view.findViewById<LinearProgressIndicator>(R.id.progressPresupuesto)
            progress?.progress = porcentaje
            val tv = view.findViewById<TextView>(R.id.tvPorcentajePresupuesto)
            tv?.text = "$porcentaje%"
        }

        presupuestoViewModel.alertaPresupuesto.observe(viewLifecycleOwner) { alerta ->
            val alertView = view.findViewById<View>(R.id.icAlertaPresupuesto)
            alertView?.visibility = if (alerta == true) View.VISIBLE else View.GONE
        }
    }
}