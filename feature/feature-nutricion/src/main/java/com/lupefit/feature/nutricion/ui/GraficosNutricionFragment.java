package com.lupefit.feature.nutricion.ui;

import android.content.Context;
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
import java.util.Locale;
import java.util.Map;
import java.util.function.ToDoubleFunction;

import com.google.android.material.color.MaterialColors;
import com.lupefit.core.domain.model.MacroTotals;

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
        viewModel.history.observe(getViewLifecycleOwner(), this::renderCharts);
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
        setupLineChart(binding.chartCalorias);
        setupLineChart(binding.chartProteinas);
        setupLineChart(binding.chartCarbohidratos);
        setupLineChart(binding.chartGrasas);
        setupPieChart(binding.chartMacroDistribution);
    }

    private void setupLineChart(LineChart chart) {
        chart.setNoDataText(getString(R.string.chart_sin_datos));
        chart.setNoDataTextColor(onSurface(chart));
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(onSurface(chart));
        xAxis.setGranularity(1f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(MaterialColors.getColor(chart, com.google.android.material.R.attr.colorOutlineVariant));
        leftAxis.setTextColor(onSurface(chart));
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
    }

    private void setupPieChart(PieChart chart) {
        chart.setNoDataText(getString(R.string.chart_sin_datos));
        chart.setNoDataTextColor(onSurface(chart));
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(45f);
        chart.setUsePercentValues(true);
        chart.setDrawEntryLabels(true);
        chart.setDrawEntryLabels(false);
        chart.setHoleColor(MaterialColors.getColor(chart, com.google.android.material.R.attr.colorSurface));
        chart.getLegend().setTextColor(onSurface(chart));
        chart.setEntryLabelTextSize(12f);

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    private int onSurface(View view) {
        return MaterialColors.getColor(view, com.google.android.material.R.attr.colorOnSurface);
    }

    private void renderCharts(Map<Integer, MacroTotals> history) {
        int protein = requireContext().getColor(com.lupefit.core.ui.R.color.macro_protein);
        int carbs = requireContext().getColor(com.lupefit.core.ui.R.color.macro_carbs);
        int fat = requireContext().getColor(com.lupefit.core.ui.R.color.macro_fat);
        renderLine(binding.chartCalorias, history, MacroTotals::getCalorias,
                MaterialColors.getColor(binding.chartCalorias, androidx.appcompat.R.attr.colorPrimary));
        renderLine(binding.chartProteinas, history, MacroTotals::getProteinas, protein);
        renderLine(binding.chartCarbohidratos, history, MacroTotals::getCarbohidratos, carbs);
        renderLine(binding.chartGrasas, history, MacroTotals::getGrasas, fat);

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        addSlice(entries, colors, history, MacroTotals::getProteinas, R.string.chart_proteinas, protein);
        addSlice(entries, colors, history, MacroTotals::getCarbohidratos, R.string.chart_carbohidratos, carbs);
        addSlice(entries, colors, history, MacroTotals::getGrasas, R.string.chart_grasas, fat);
        if (entries.isEmpty()) {
            binding.chartMacroDistribution.clear();
            return;
        }
        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);
        set.setDrawValues(false);
        binding.chartMacroDistribution.setData(new PieData(set));
        binding.chartMacroDistribution.invalidate();
    }

    private void addSlice(List<PieEntry> entries, List<Integer> colors,
                          Map<Integer, MacroTotals> history, ToDoubleFunction<MacroTotals> metric,
                          int label, int color) {
        float total = 0;
        for (MacroTotals day : history.values()) total += metric.applyAsDouble(day);
        if (total > 0) {
            entries.add(new PieEntry(total, getString(label)));
            colors.add(color);
        }
    }

    private void renderLine(LineChart chart, Map<Integer, MacroTotals> history,
                            ToDoubleFunction<MacroTotals> metric, int color) {
        if (history.isEmpty()) {
            chart.clear();
            return;
        }
        List<Entry> values = new ArrayList<>();
        List<Integer> dates = new ArrayList<>();
        for (Map.Entry<Integer, MacroTotals> day : history.entrySet()) {
            values.add(new Entry(dates.size(), (float) metric.applyAsDouble(day.getValue())));
            dates.add(day.getKey());
        }
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = Math.round(value);
                if (value != index || index < 0 || index >= dates.size()) return "";
                int date = dates.get(index);
                return String.format(Locale.getDefault(), "%02d/%02d", date % 100, date / 100 % 100);
            }
        });
        LineDataSet set = new LineDataSet(values, "");
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        chart.setData(new LineData(set));
        chart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}