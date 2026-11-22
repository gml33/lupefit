package com.lupefit.feature.insights.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lupefit.core.ui.R;
import com.lupefit.feature.insights.databinding.FragmentTendenciasBinding;
import com.lupefit.feature.insights.viewmodel.TendenciasViewModel;

import java.util.ArrayList;
import java.util.List;

public class TendenciasFragment extends Fragment {

    private FragmentTendenciasBinding binding;
    private TendenciasViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTendenciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new TendenciasViewModel.Factory(requireContext())).get(TendenciasViewModel.class);

        setupToolbar();
        setupCharts();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.setTitle(getString(R.string.tendencias));
    }

    private void setupCharts() {
        configurarGrafico(binding.graficoCalorias, R.color.macro_carbs);
        configurarGrafico(binding.graficoPeso, R.color.macro_protein);
        configurarGrafico(binding.graficoProteinas, R.color.macro_fat);
    }

    private void configurarGrafico(LineChart chart, int colorRes) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(getResources().getColor(R.color.md_theme_surface));

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value + 1);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.md_theme_outline));
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
    }

    private void actualizarGrafico(LineChart chart, List<Entry> datos, int colorRes) {
        LineDataSet dataSet = new LineDataSet(datos, "");
        dataSet.setColor(getResources().getColor(colorRes));
        dataSet.setValueTextColor(getResources().getColor(colorRes));
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleColor(getResources().getColor(colorRes));
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(colorRes));
        dataSet.setFillAlpha(60);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    private void setupClickListeners() {
        binding.chipGroupPeriodo.setOnCheckedChangeListener((group, checkedId) -> {
            TendenciasViewModel.Periodo periodo = TendenciasViewModel.Periodo.SEMANA;
            if (checkedId == R.id.chipMes) {
                periodo = TendenciasViewModel.Periodo.MES;
            } else if (checkedId == R.id.chipTrimestre) {
                periodo = TendenciasViewModel.Periodo.TRIMESTRE;
            }
            viewModel.setPeriodo(periodo);
        });
    }

    private void setupObservers() {
        viewModel.cargando.observe(getViewLifecycleOwner(), cargando -> {
            binding.progressBar.setVisibility(cargando != null && cargando ? View.VISIBLE : View.GONE);
        });

        viewModel.datosCalorias.observe(getViewLifecycleOwner(), datos -> {
            if (datos != null) {
                actualizarGrafico(binding.graficoCalorias, datos, R.color.macro_carbs);
            }
        });

        viewModel.datosPeso.observe(getViewLifecycleOwner(), datos -> {
            if (datos != null) {
                actualizarGrafico(binding.graficoPeso, datos, R.color.macro_protein);
            }
        });

        viewModel.datosProteinas.observe(getViewLifecycleOwner(), datos -> {
            if (datos != null) {
                actualizarGrafico(binding.graficoProteinas, datos, R.color.macro_fat);
            }
        });

        viewModel.periodoSeleccionado.observe(getViewLifecycleOwner(), periodo -> {
            int chipId = R.id.chipSemana;
            if (periodo == TendenciasViewModel.Periodo.MES) chipId = R.id.chipMes;
            else if (periodo == TendenciasViewModel.Periodo.TRIMESTRE) chipId = R.id.chipTrimestre;
            binding.chipGroupPeriodo.check(chipId);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}