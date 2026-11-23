package com.lupefit.feature.ejercicio.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.CountDownTimer;
import android.util.TypedValue;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lupefit.feature.ejercicio.R;
import com.lupefit.feature.ejercicio.databinding.FragmentEjercicioHomeBinding;
import com.lupefit.feature.ejercicio.viewmodel.EjercicioHomeViewModel;

public class EjercicioHomeFragment extends Fragment {

    private FragmentEjercicioHomeBinding binding;
    private EjercicioHomeViewModel viewModel;
    private RecentWorkoutAdapter recentAdapter;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEjercicioHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        com.lupefit.core.domain.provider.WorkoutSummaryProvider provider =
                (com.lupefit.core.domain.provider.WorkoutSummaryProvider) requireActivity().getApplication();
        com.lupefit.core.domain.provider.CurrentUserProvider userProvider =
                (com.lupefit.core.domain.provider.CurrentUserProvider) requireActivity().getApplication();
        viewModel = new EjercicioHomeViewModel(provider, userProvider.getCurrentUserId());

        setupToolbar();
        setupClickListeners();
        setupObservers();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
    }

    private void setupClickListeners() {
        // Quick action cards
        binding.cardBuscarEjercicio.setOnClickListener(v -> {
            navController.navigate(R.id.action_ejercicioHomeFragment_to_buscarEjercicioFragment);
        });

        binding.cardNuevaRutina.setOnClickListener(v -> {
            Bundle arguments = new Bundle();
            arguments.putBoolean("create", true);
            navController.navigate(R.id.action_ejercicioHomeFragment_to_rutinaDetalleFragment, arguments);
        });

        binding.cardWodTimer.setOnClickListener(v -> showWodTimer());

        binding.cardMisRutinas.setOnClickListener(v -> {
            navController.navigate(R.id.action_ejercicioHomeFragment_to_rutinaDetalleFragment);
        });
    }

    private void showUnavailable() {
        // Compatibilidad con layouts/artefactos antiguos: el acceso WOD siempre abre el temporizador.
        showWodTimer();
    }

    private void showWodTimer() {
        LinearLayout box = new LinearLayout(requireContext());
        box.setOrientation(LinearLayout.VERTICAL); box.setPadding(32, 16, 32, 8);
        TextView formatLabel = fieldLabel("Formato"); box.addView(formatLabel);
        Spinner format = new Spinner(requireContext());
        format.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{"AMRAP", "EMOM", "FOR TIME", "TABATA"}));
        LinearLayout.LayoutParams selectorParams = new LinearLayout.LayoutParams(-1, 64); selectorParams.bottomMargin = 8;
        box.addView(format, selectorParams);
        box.addView(fieldLabel("Duración (minutos)"));
        EditText durationInput = numericField("10");
        box.addView(durationInput, new LinearLayout.LayoutParams(-1, 60));
        box.addView(fieldLabel("Rondas (EMOM/Tabata)"));
        EditText roundsInput = numericField("8");
        box.addView(roundsInput, new LinearLayout.LayoutParams(-1, 60));
        TextView timer = new TextView(requireContext());
        timer.setText("10:00"); timer.setGravity(android.view.Gravity.CENTER);
        timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48); timer.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams timerParams = new LinearLayout.LayoutParams(-1, 112); timerParams.topMargin = 8; box.addView(timer, timerParams);
        MaterialButton action = new MaterialButton(requireContext()); action.setText("Iniciar"); action.setMinHeight(56);
        box.addView(action, new LinearLayout.LayoutParams(-1, 56));
        final long[] remaining = {600_000L}; final CountDownTimer[] countdown = {null};
        final int[] round = {1};
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext()).setTitle("WOD Timer").setView(box).setNegativeButton("Cerrar", null).create();
        action.setOnClickListener(v -> {
            if (countdown[0] == null) {
                int minutes = Math.max(1, parseInt(durationInput.getText().toString(), 10));
                int rounds = Math.max(1, parseInt(roundsInput.getText().toString(), 8));
                String selected = String.valueOf(format.getSelectedItem());
                remaining[0] = (selected.equals("TABATA") ? 20_000L : minutes * 60_000L);
                action.setText("Pausar");
                countdown[0] = new CountDownTimer(remaining[0], 1000) {
                    public void onTick(long millis) { remaining[0] = millis; timer.setText(String.format(java.util.Locale.getDefault(), "%02d:%02d", millis/60000, (millis/1000)%60)); }
                    public void onFinish() {
                        if (selected.equals("TABATA") && round[0] < rounds) { round[0]++; remaining[0] = 20_000L; start(); return; }
                        timer.setText("00:00"); action.setText("Reiniciar"); countdown[0] = null;
                    }
                }.start();
            } else { countdown[0].cancel(); countdown[0] = null; action.setText("Reanudar"); }
        });
        dialog.setOnDismissListener(d -> { if (countdown[0] != null) countdown[0].cancel(); }); dialog.show();
    }

    private int parseInt(String value, int fallback) { try { return Integer.parseInt(value); } catch (NumberFormatException ignored) { return fallback; } }

    private TextView fieldLabel(String text) {
        TextView label = new TextView(requireContext());
        label.setText(text); label.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge);
        label.setTextColor(requireContext().getColor(com.lupefit.core.ui.R.color.md_theme_onSurfaceVariant));
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        label.setGravity(android.view.Gravity.CENTER_VERTICAL | android.view.Gravity.START);
        label.setIncludeFontPadding(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 36); params.topMargin = 4;
        label.setLayoutParams(params); return label;
    }

    private EditText numericField(String value) {
        EditText field = new EditText(requireContext());
        field.setInputType(2); field.setText(value); field.setSingleLine(true);
        field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        field.setGravity(android.view.Gravity.CENTER_VERTICAL | android.view.Gravity.START);
        field.setIncludeFontPadding(true);
        field.setPadding(0, 0, 0, 0);
        field.setMinHeight(60);
        return field;
    }

    public void openWodTimer(View view) {
        showWodTimer();
    }

    private void setupObservers() {
        recentAdapter = new RecentWorkoutAdapter();
        binding.rvRecentWorkouts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentWorkouts.setAdapter(recentAdapter);
        viewModel.recentWorkouts.observe(getViewLifecycleOwner(), workouts -> {
            boolean hasData = workouts != null && !workouts.isEmpty();
            recentAdapter.submitList(workouts);
            binding.emptyState.getRoot().setVisibility(hasData ? View.GONE : View.VISIBLE);
            binding.rvRecentWorkouts.setVisibility(hasData ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
