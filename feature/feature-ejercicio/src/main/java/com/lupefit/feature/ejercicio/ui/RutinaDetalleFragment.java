package com.lupefit.feature.ejercicio.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lupefit.core.domain.provider.RoutineRepositoryProvider;
import com.lupefit.core.domain.repository.RoutineRepository;
import com.lupefit.feature.ejercicio.R;
import com.lupefit.feature.ejercicio.databinding.FragmentRutinaDetalleBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RutinaDetalleFragment extends Fragment {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private FragmentRutinaDetalleBinding binding;
    private RoutineRepository repository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRutinaDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = ((RoutineRepositoryProvider) requireContext().getApplicationContext()).getRoutineRepository();
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.newRoutine.setOnClickListener(v -> openEditor());
        refresh(() -> {});
        if (savedInstanceState == null && getArguments() != null && getArguments().getBoolean("create")) {
            openEditor();
        }
    }

    private void refresh(Runnable change) {
        View currentView = binding.getRoot();
        executor.execute(() -> {
            try {
                change.run();
                List<RoutineRepository.Routine> routines = repository.list();
                currentView.post(() -> {
                    if (binding != null && binding.getRoot() == currentView) {
                        render(routines);
                    }
                });
            } catch (RuntimeException error) {
                currentView.post(() -> {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), R.string.routine_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void render(List<RoutineRepository.Routine> routines) {
        binding.routineItems.removeAllViews();
        if (routines.isEmpty()) {
            android.widget.TextView empty = new android.widget.TextView(requireContext());
            empty.setTextAppearance(com.lupefit.core.ui.R.style.TextAppearance_LuPeFit_BodyMedium);
            empty.setText(R.string.routine_empty);
            empty.setPadding(0, getResources().getDimensionPixelSize(R.dimen.spacing_m), 0, 0);
            binding.routineItems.addView(empty);
        }
        for (RoutineRepository.Routine routine : routines) {
            MaterialButton row = new MaterialButton(new ContextThemeWrapper(
                    requireContext(), R.style.Widget_LuPeFit_Button_Outlined));
            row.setText(routine.title);
            row.setOnClickListener(v -> showRoutine(routine));
            binding.routineItems.addView(row, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void showRoutine(RoutineRepository.Routine routine) {
        String details = routine.exercises;
        if (routine.completedAt > 0) {
            details += "\n\n" + getString(R.string.routine_completed,
                    DateFormat.getDateTimeInstance().format(new Date(routine.completedAt)));
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(routine.title)
                .setMessage(details)
                .setPositiveButton(R.string.routine_finish, (dialog, which) ->
                        refresh(() -> repository.markCompleted(routine.id)))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void openEditor() {
        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_screen);
        form.setPadding(padding, 0, padding, 0);
        TextInputEditText title = addField(form, R.string.nueva_rutina_titulo, InputType.TYPE_CLASS_TEXT);
        TextInputEditText exercises = addField(form, R.string.routine_exercises_hint,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.nueva_rutina)
                .setView(form)
                .setPositiveButton(R.string.nueva_rutina_guardar, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = String.valueOf(title.getText()).trim();
            String items = String.valueOf(exercises.getText()).trim();
            if (name.isEmpty() || items.isEmpty()) {
                exercises.setError(getString(R.string.routine_required));
                return;
            }
            refresh(() -> repository.save(name, items));
            dialog.dismiss();
        }));
        dialog.show();
    }

    private TextInputEditText addField(LinearLayout form, int label, int inputType) {
        TextInputLayout field = new TextInputLayout(requireContext());
        field.setHint(getString(label));
        TextInputEditText input = new TextInputEditText(field.getContext());
        input.setInputType(inputType);
        field.addView(input);
        form.addView(field);
        return input;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }
}
