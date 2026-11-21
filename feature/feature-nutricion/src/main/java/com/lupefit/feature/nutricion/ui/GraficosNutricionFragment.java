package com.lupefit.feature.nutricion.ui;

import android.content.Context;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lupefit.feature.nutricion.R;
import com.lupefit.feature.nutricion.databinding.FragmentGraficosNutricionBinding;
import com.lupefit.feature.nutricion.viewmodel.GraficosNutricionViewModel;

import java.util.ArrayList;
import java.util.List;

public class GraficosNutricionFragment extends Fragment {

    private FragmentGraficosNutricionBinding binding;
    private GraficosNutricionViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGraficosNutricionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new GraficosNutricionViewModel.Factory(context)).get(GraficosNutricionViewModel.class);

        setupToolbar();
        setupChipGroup();
        setupCharts();
        setupMockData(); // TODO: Replace with real data from ViewModel
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupChipGroup() {
        binding.chipGroupPeriod.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                com.google.android.material.chip.Chip chip = group.findViewById(checkedIds.get(0));
                if (chip != null && chip.getTag() != null) {
                    int days = Integer.parseInt(chip.getTag().toString());
                    viewModel.setPeriod(days);
                }
            }
        });
    }

    private void setupCharts() {
        setupLineChart(binding.chartCalorias, 0xFF10B981);
        setupLineChart(binding.chartProteinas, 0xFF2563EB);
        setupLineChart(binding.chartCarbohidratos, 0xFFD97706);
        setupLineChart(binding.chartGrasas, 0xFF7C3AED);
        setupPieChart(binding.chartMacroDistribution);
    }

    private void setupLineChart(LineChart chart, int color) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setGranularity(1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
    }

    private void setupPieChart(PieChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(45f);
        chart.setUsePercentValues(true);
        chart.setDrawEntryLabels(true);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    private void setupMockData() {
        // Calorías trend
        List<Entry> caloriasEntries = new ArrayList<>();
        caloriasEntries.add(new Entry(1, 1850));
        caloriasEntries.add(new Entry(2, 1920));
        caloriasEntries.add(new Entry(3, 1780));
        caloriasEntries.add(new Entry(4, 2010));
        caloriasEntries.add(new Entry(5, 1890));
        caloriasEntries.add(new Entry(6, 1950));
        caloriasEntries.add(new Entry(7, 2100));

        LineDataSet caloriasSet = new LineDataSet(caloriasEntries, "Calorías");
        caloriasSet.setColor(0xFF10B981);
        caloriasSet.setCircleColor(0xFF10B981);
        caloriasSet.setLineWidth(2f);
        caloriasSet.setCircleRadius(4f);
        caloriasSet.setDrawValues(true);
        caloriasSet.setValueTextSize(10f);
        caloriasSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData caloriasData = new LineData(caloriasSet);
        binding.chartCalorias.setData(caloriasData);
        binding.chartCalorias.invalidate();

        // Proteínas
        List<Entry> proteinasEntries = new ArrayList<>();
        proteinasEntries.add(new Entry(1, 120));
        proteinasEntries.add(new Entry(2, 135));
        proteinasEntries.add(new Entry(3, 110));
        proteinasEntries.add(new Entry(4, 140));
        proteinasEntries.add(new Entry(5, 125));
        proteinasEntries.add(new Entry(6, 130));
        proteinasEntries.add(new Entry(7, 145));

        LineDataSet proteinasSet = new LineDataSet(proteinasEntries, "Proteínas");
        proteinasSet.setColor(0xFF2563EB);
        proteinasSet.setCircleColor(0xFF2563EB);
        proteinasSet.setLineWidth(2f);
        proteinasSet.setCircleRadius(4f);
        proteinasSet.setDrawValues(true);
        proteinasSet.setValueTextSize(10f);
        proteinasSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData proteinasData = new LineData(proteinasSet);
        binding.chartProteinas.setData(proteinasData);
        binding.chartProteinas.invalidate();

        // Carbohidratos
        List<Entry> carbsEntries = new ArrayList<>();
        carbsEntries.add(new Entry(1, 220));
        carbsEntries.add(new Entry(2, 240));
        carbsEntries.add(new Entry(3, 200));
        carbsEntries.add(new Entry(4, 260));
        carbsEntries.add(new Entry(5, 230));
        carbsEntries.add(new Entry(6, 245));
        carbsEntries.add(new Entry(7, 270));

        LineDataSet carbsSet = new LineDataSet(carbsEntries, "Carbohidratos");
        carbsSet.setColor(0xFFD97706);
        carbsSet.setCircleColor(0xFFD97706);
        carbsSet.setLineWidth(2f);
        carbsSet.setCircleRadius(4f);
        carbsSet.setDrawValues(true);
        carbsSet.setValueTextSize(10f);
        carbsSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData carbsData = new LineData(carbsSet);
        binding.chartCarbohidratos.setData(carbsData);
        binding.chartCarbohidratos.invalidate();

        // Grasas
        List<Entry> grasasEntries = new ArrayList<>();
        grasasEntries.add(new Entry(1, 65));
        grasasEntries.add(new Entry(2, 70));
        grasasEntries.add(new Entry(3, 60));
        grasasEntries.add(new Entry(4, 75));
        grasasEntries.add(new Entry(5, 68));
        grasasEntries.add(new Entry(6, 72));
        grasasEntries.add(new Entry(7, 80));

        LineDataSet grasasSet = new LineDataSet(grasasEntries, "Grasas");
        grasasSet.setColor(0xFF7C3AED);
        grasasSet.setCircleColor(0xFF7C3AED);
        grasasSet.setLineWidth(2f);
        grasasSet.setCircleRadius(4f);
        grasasSet.setDrawValues(true);
        grasasSet.setValueTextSize(10f);
        grasasSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData grasasData = new LineData(grasasSet);
        binding.chartGrasas.setData(grasasData);
        binding.chartGrasas.invalidate();

        // Macro Distribution Pie Chart
        List<PieEntry> macroEntries = new ArrayList<>();
        macroEntries.add(new PieEntry(120f, "Proteínas"));
        macroEntries.add(new PieEntry(240f, "Carbohidratos"));
        macroEntries.add(new PieEntry(65f, "Grasas"));

        PieDataSet macroSet = new PieDataSet(macroEntries, "");
        macroSet.setColors(new int[]{0xFF2563EB, 0xFFD97706, 0xFF7C3AED});
        macroSet.setValueTextSize(12f);
        macroSet.setValueTextColor(Color.WHITE);
        macroSet.setSliceSpace(2f);

        PieData macroData = new PieData(macroSet);
        macroData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f%%", value);
            }
        });
        binding.chartMacroDistribution.setData(macroData);
        binding.chartMacroDistribution.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}