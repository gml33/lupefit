package com.lupefit.feature.insights.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lupefit.feature.insights.R
import com.lupefit.feature.insights.databinding.FragmentTendenciasBinding
import com.lupefit.feature.insights.viewmodel.TendenciasViewModel

class TendenciasFragment : Fragment() {

    private var _binding: FragmentTendenciasBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TendenciasViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTendenciasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this, TendenciasViewModel.Factory(requireContext())).get(TendenciasViewModel::class.java)

        setupToolbar()
        setupCharts()
        setupClickListeners()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.toolbar.title = getString(R.string.tendencias)
    }

    private fun setupCharts() {
        configurarGrafico(binding.graficoCalorias, R.color.macro_carbs)
        configurarGrafico(binding.graficoPeso, R.color.macro_protein)
        configurarGrafico(binding.graficoProteinas, R.color.macro_fat)
    }

    private fun configurarGrafico(chart: LineChart, colorRes: Int) {
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setDrawGridBackground(false)
        chart.setBackgroundColor(resources.getColor(R.color.md_theme_surface))

        val legend = chart.legend
        legend.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return (value.toInt() + 1).toString()
            }
        }

        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = resources.getColor(R.color.md_theme_outline)
        leftAxis.axisMinimum = 0f

        chart.axisRight.isEnabled = false
    }

    private fun actualizarGrafico(chart: LineChart, datos: List<Entry>, colorRes: Int) {
        val dataSet = LineDataSet(datos, "")
        val color = resources.getColor(colorRes)
        dataSet.color = color
        dataSet.valueTextColor = color
        dataSet.lineWidth = 2.5f
        dataSet.setCircleColor(color)
        dataSet.circleRadius = 4f
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = color
        dataSet.fillAlpha = 60
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.cubicIntensity = 0.2f

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    private fun setupClickListeners() {
        binding.chipGroupPeriodo.setOnCheckedChangeListener { _, checkedId ->
            val periodo = when (checkedId) {
                R.id.chipMes -> TendenciasViewModel.Periodo.MES
                R.id.chipTrimestre -> TendenciasViewModel.Periodo.TRIMESTRE
                else -> TendenciasViewModel.Periodo.SEMANA
            }
            viewModel.setPeriodo(periodo)
        }
    }

    private fun setupObservers() {
        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando == true) View.VISIBLE else View.GONE
        }

        viewModel.datosCalorias.observe(viewLifecycleOwner) { datos ->
            datos?.let { actualizarGrafico(binding.graficoCalorias, it.toList(), R.color.macro_carbs) }
        }

        viewModel.datosPeso.observe(viewLifecycleOwner) { datos ->
            datos?.let { actualizarGrafico(binding.graficoPeso, it.toList(), R.color.macro_protein) }
        }

        viewModel.datosProteinas.observe(viewLifecycleOwner) { datos ->
            datos?.let { actualizarGrafico(binding.graficoProteinas, it.toList(), R.color.macro_fat) }
        }

        viewModel.periodoSeleccionado.observe(viewLifecycleOwner) { periodo ->
            val chipId = when (periodo) {
                TendenciasViewModel.Periodo.MES -> R.id.chipMes
                TendenciasViewModel.Periodo.TRIMESTRE -> R.id.chipTrimestre
                else -> R.id.chipSemana
            }
            binding.chipGroupPeriodo.check(chipId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}