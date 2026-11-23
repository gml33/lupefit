package com.lupefit.feature.recetas.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.model.Receta;
import com.lupefit.feature.recetas.R;
import com.lupefit.feature.recetas.databinding.FragmentRecetasHomeBinding;
import com.lupefit.feature.recetas.databinding.ItemRecetaBinding;
import com.lupefit.feature.recetas.viewmodel.RecetasHomeViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class RecetasHomeFragment extends Fragment {

    private FragmentRecetasHomeBinding binding;
    private RecetasHomeViewModel viewModel;
    private NavController navController;
    private final RecipeAdapter adapter = new RecipeAdapter();
    private List<Receta> recipes = new ArrayList<>();
    private String category;
    private int sortOrder = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecetasHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new RecetasHomeViewModel.Factory(requireContext()))
                .get(RecetasHomeViewModel.class);

        binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        binding.toolbar.getMenu().add(R.string.colecciones).setOnMenuItemClickListener(item -> {
            showCollections();
            return true;
        });
        binding.toolbar.getMenu().add(R.string.importar_receta).setOnMenuItemClickListener(item -> {
            navController.navigate(R.id.action_recetasHomeFragment_to_importarRecetaFragment);
            return true;
        });

        binding.rvRecetas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecetas.setAdapter(adapter);
        binding.rvRecetas.setNestedScrollingEnabled(false);
        binding.btnCrearReceta.setOnClickListener(v -> openEditor(null));
        binding.fabCrearReceta.setOnClickListener(v -> openEditor(null));
        binding.emptyState.btnFirstRecipe.setOnClickListener(v -> openEditor(null));
        setupFilters();

        viewModel.recetas.observe(getViewLifecycleOwner(), values -> {
            recipes = values;
            renderRecipes();
        });
        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadRecetas();
    }

    private void setupFilters() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                renderRecipes();
            }
            @Override public void afterTextChanged(Editable s) { }
        });
        binding.chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> renderRecipes());
        String[] orders = {getString(R.string.ordenar_nombre), getString(R.string.ordenar_fecha),
                getString(R.string.ordenar_calorias), getString(R.string.ordenar_tiempo)};
        binding.etSort.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, orders));
        binding.etSort.setText(orders[sortOrder], false);
        binding.etSort.setOnItemClickListener((parent, view, position, id) -> {
            sortOrder = position;
            renderRecipes();
        });
    }

    private void renderRecipes() {
        if (binding == null) return;
        String query = normalized(String.valueOf(binding.etSearch.getText()).trim());
        int filter = binding.chipGroupFilters.getCheckedChipId();
        List<Receta> visible = new ArrayList<>();
        long recent = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30);
        for (Receta recipe : recipes) {
            if (category != null && !category.equals(recipe.getCategoria())) continue;
            if (filter == R.id.chipFavoritas && !recipe.isEsFavorita()) continue;
            if (filter == R.id.chipRecientes && recipe.getTimestamp() < recent) continue;
            if (!normalized(recipe.getNombre()).contains(query) && recipe.getIngredientes().stream()
                    .noneMatch(ingredient -> normalized(ingredient.getAlimentoNombre()).contains(query))) continue;
            visible.add(recipe);
        }
        Comparator<Receta> comparator;
        switch (sortOrder) {
            case 0: comparator = Comparator.comparing(recipe -> normalized(recipe.getNombre())); break;
            case 2: comparator = Comparator.comparingInt(recipe -> recipe.getCaloriasPorPorcion() < 0
                    ? Integer.MAX_VALUE : recipe.getCaloriasPorPorcion()); break;
            case 3: comparator = Comparator.comparingInt(Receta::getTiempoTotalMin); break;
            default: comparator = Comparator.comparingLong(Receta::getTimestamp).reversed(); break;
        }
        visible.sort(comparator);
        adapter.items = visible;
        adapter.notifyDataSetChanged();
        binding.emptyState.getRoot().setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
        binding.contentContainer.setVisibility(recipes.isEmpty() ? View.GONE : View.VISIBLE);
        binding.tvRecetaSinResultados.setVisibility(!recipes.isEmpty() && visible.isEmpty()
                ? View.VISIBLE : View.GONE);
    }

    private String normalized(String text) {
        return text == null ? "" : java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
    }

    private void showCollections() {
        TreeSet<String> categories = new TreeSet<>();
        for (Receta recipe : recipes) {
            if (recipe.getCategoria() != null && !recipe.getCategoria().isEmpty()) categories.add(recipe.getCategoria());
        }
        List<String> choices = new ArrayList<>();
        choices.add(getString(R.string.filtro_todas));
        choices.addAll(categories);
        int selected = category == null ? 0 : choices.indexOf(category);
        new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.colecciones)
                .setSingleChoiceItems(choices.toArray(new String[0]), selected, (dialog, position) -> {
                    category = position == 0 ? null : choices.get(position);
                    renderRecipes();
                    dialog.dismiss();
                }).show();
    }

    private void openEditor(Receta recipe) {
        Bundle arguments = new Bundle();
        if (recipe != null) arguments.putParcelable("receta", recipe);
        navController.navigate(R.id.action_recetasHomeFragment_to_crearRecetaFragment, arguments);
    }

    private void showDetails(Receta recipe) {
        StringBuilder details = new StringBuilder();
        if (recipe.getDescripcion() != null && !recipe.getDescripcion().isEmpty()) {
            details.append(recipe.getDescripcion()).append("\n\n");
        }
        details.append(getString(R.string.receta_detalle_ingredientes)).append('\n');
        for (IngredienteReceta ingredient : recipe.getIngredientes()) {
            details.append(String.format(Locale.getDefault(), "• %.2f %s %s\n",
                    ingredient.getCantidad(), ingredient.getUnidad(), ingredient.getAlimentoNombre()));
        }
        details.append('\n').append(getString(R.string.receta_detalle_instrucciones)).append('\n')
                .append(recipe.getInstrucciones()).append("\n\n");
        if (recipe.getCaloriasPorPorcion() >= 0) {
            details.append(getString(R.string.receta_detalle_info_nutricional)).append('\n')
                    .append(getString(R.string.calorias_formato, recipe.getCaloriasPorPorcion())).append('\n')
                    .append(getString(R.string.receta_detalle_macros, recipe.getProteinasPorPorcion(),
                            recipe.getCarbohidratosPorPorcion(), recipe.getGrasasPorPorcion(),
                            recipe.getFibraPorPorcion()));
        } else {
            details.append(getString(R.string.receta_nutricion_no_disponible));
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(recipe.getNombre())
                .setMessage(details)
                .setPositiveButton(R.string.receta_acciones,
                        (dialog, choice) -> showRecipeActions(recipe, details.toString()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showRecipeActions(Receta recipe, String details) {
        int[] actions = {R.string.receta_detalle_agregar_diario, R.string.receta_detalle_editar,
                R.string.receta_detalle_compartir, R.string.receta_detalle_eliminar};
        String[] labels = java.util.Arrays.stream(actions).mapToObj(this::getString).toArray(String[]::new);
        new MaterialAlertDialogBuilder(requireContext()).setTitle(recipe.getNombre())
                .setItems(labels, (dialog, position) -> {
                    switch (position) {
                        case 0: recordPortion(recipe); break;
                        case 1: openEditor(recipe); break;
                        case 2:
                            Intent share = new Intent(Intent.ACTION_SEND).setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, recipe.getNombre() + "\n\n" + details);
                            startActivity(Intent.createChooser(share, getString(R.string.receta_compartir)));
                            break;
                        case 3: confirmDelete(recipe); break;
                        default: break;
                    }
                }).show();
    }

    private void recordPortion(Receta recipe) {
        if (recipe.getCaloriasPorPorcion() < 0 || recipe.getPorciones() <= 0
                || recipe.getIngredientes().isEmpty()) {
            Toast.makeText(requireContext(), R.string.receta_nutricion_no_disponible, Toast.LENGTH_LONG).show();
            return;
        }
        double totalGrams = 0;
        for (IngredienteReceta ingredient : recipe.getIngredientes()) {
            if (!"g".equals(ingredient.getUnidad()) && !"kg".equals(ingredient.getUnidad())) {
                Toast.makeText(requireContext(), R.string.receta_nutricion_no_disponible, Toast.LENGTH_LONG).show();
                return;
            }
            totalGrams += ingredient.getCantidad() * ("kg".equals(ingredient.getUnidad()) ? 1000 : 1);
        }
        double portionGrams = totalGrams / recipe.getPorciones();
        if (!(portionGrams > 0) || !Double.isFinite(portionGrams)) return;
        String[] mealKeys = {"desayuno", "almuerzo", "cena", "snack"};
        String[] labels = {getString(R.string.categoria_desayuno), getString(R.string.categoria_almuerzo),
                getString(R.string.categoria_cena), getString(R.string.categoria_snack)};
        new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.receta_registrar_porcion)
                .setItems(labels, (dialog, position) -> {
                    Calendar today = Calendar.getInstance();
                    DiarioEntry entry = new DiarioEntry();
                    entry.setFoodName(recipe.getNombre());
                    entry.setCantidad(portionGrams);
                    entry.setCalorias(recipe.getCaloriasPorPorcion());
                    entry.setProteinas(recipe.getProteinasPorPorcion());
                    entry.setCarbohidratos(recipe.getCarbohidratosPorPorcion());
                    entry.setGrasas(recipe.getGrasasPorPorcion());
                    entry.setFibra(recipe.getFibraPorPorcion());
                    entry.setTipoComida(mealKeys[position]);
                    entry.setTimestamp(today.getTimeInMillis());
                    entry.setLastModified(today.getTimeInMillis());
                    entry.setDateKey(today.get(Calendar.YEAR) * 10000
                            + (today.get(Calendar.MONTH) + 1) * 100 + today.get(Calendar.DAY_OF_MONTH));
                    entry.setSourceType("manual");
                    ((DiarioRepositoryProvider) requireContext().getApplicationContext())
                            .getDiarioRepository().insert(entry);
                    navController.navigate(Uri.parse("lupefit://diario"));
                }).show();
    }

    private void confirmDelete(Receta recipe) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.receta_borrar_confirmar)
                .setMessage(recipe.getNombre())
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.receta_detalle_eliminar, (dialog, choice) -> viewModel.deleteReceta(recipe.getId()))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvRecetas.setAdapter(null);
        binding = null;
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
        List<Receta> items = new ArrayList<>();

        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipeHolder(ItemRecetaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            Receta recipe = items.get(position);
            ItemRecetaBinding row = holder.row;
            row.setReceta(recipe);
            row.getRoot().setOnClickListener(v -> showDetails(recipe));
            row.btnFavorite.setOnClickListener(v -> viewModel.toggleFavorite(recipe));
            row.ivRecetaImage.setImageResource(com.lupefit.core.ui.R.drawable.ic_book);
            if (recipe.getFotoUri() != null && !recipe.getFotoUri().isEmpty()) {
                row.ivRecetaImage.setImageURI(Uri.parse(recipe.getFotoUri()));
            }
            row.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class RecipeHolder extends RecyclerView.ViewHolder {
        final ItemRecetaBinding row;

        RecipeHolder(ItemRecetaBinding row) {
            super(row.getRoot());
            this.row = row;
        }
    }
}
