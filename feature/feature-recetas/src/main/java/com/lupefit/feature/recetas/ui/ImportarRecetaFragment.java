package com.lupefit.feature.recetas.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lupefit.core.domain.model.IngredienteReceta;
import com.lupefit.core.domain.model.Receta;
import com.lupefit.feature.recetas.R;
import com.lupefit.feature.recetas.databinding.FragmentImportarRecetaBinding;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class ImportarRecetaFragment extends Fragment {
    private static final int MAX_BYTES = 2 * 1024 * 1024;
    private static final Pattern JSON_LD = Pattern.compile(
            "<script\\b[^>]*type\\s*=\\s*['\"]application/ld\\+json['\"][^>]*>(.*?)</script\\s*>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern QUANTITY = Pattern.compile(
            "^(\\d+(?:[.,]\\d+)?)(?:\\s*)(kg|g|ml|l|cda|cdt|taza|unidad)\\b\\s*(?:de\\s+)?(.+)$",
            Pattern.CASE_INSENSITIVE);

    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private FragmentImportarRecetaBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle state) {
        binding = FragmentImportarRecetaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle state) {
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.btnImportar.setOnClickListener(v -> importRecipe());
    }

    private void importRecipe() {
        final FragmentImportarRecetaBinding screen = binding;
        String address = String.valueOf(screen.etUrlReceta.getText()).trim();
        String content = String.valueOf(screen.etTextoReceta.getText()).trim();
        if (address.isEmpty() && content.isEmpty()) {
            screen.tilUrlReceta.setError(getString(R.string.receta_importar_error));
            return;
        }
        screen.tilUrlReceta.setError(null);
        screen.btnImportar.setEnabled(false);
        worker.execute(() -> {
            try {
                Receta draft = address.isEmpty() ? fromText(content) : fromPage(fetchPage(address));
                screen.getRoot().post(() -> {
                    if (binding != screen) return;
                    screen.btnImportar.setEnabled(true);
                    NavController nav = NavHostFragment.findNavController(this);
                    if (nav.getCurrentDestination() == null
                            || nav.getCurrentDestination().getId() != R.id.importarRecetaFragment) return;
                    Bundle args = new Bundle();
                    args.putParcelable("receta", draft);
                    nav.navigate(R.id.action_importarRecetaFragment_to_crearRecetaFragment, args);
                });
            } catch (Exception error) {
                screen.getRoot().post(() -> {
                    if (binding != screen) return;
                    screen.btnImportar.setEnabled(true);
                    Toast.makeText(requireContext(), R.string.receta_importar_error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private String fetchPage(String address) throws IOException {
        URL url = new URL(address);
        if (!"https".equals(url.getProtocol()) || url.getHost().isEmpty() || url.getUserInfo() != null) {
            throw new IOException("HTTPS required");
        }
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        try {
            if (connection.getResponseCode() != 200 || connection.getContentLength() > MAX_BYTES) {
                throw new IOException("Invalid response");
            }
            try (InputStream stream = connection.getInputStream()) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int count;
                while ((count = stream.read(buffer)) != -1) {
                    if (bytes.size() + count > MAX_BYTES) throw new IOException("Page too large");
                    bytes.write(buffer, 0, count);
                }
                return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
            }
        } finally {
            connection.disconnect();
        }
    }

    private Receta fromText(String text) {
        String[] lines = text.split("\\R", 2);
        Receta draft = new Receta();
        draft.setNombre(lines[0]);
        draft.setPorciones(1);
        draft.setInstrucciones(lines.length > 1 ? lines[1].trim() : "");
        return draft;
    }

    private Receta fromPage(String html) throws IOException {
        Matcher scripts = JSON_LD.matcher(html);
        while (scripts.find()) {
            JSONObject data;
            try {
                data = findRecipe(new JSONTokener(scripts.group(1).trim()).nextValue(), 0);
            } catch (org.json.JSONException e) {
                continue;
            }
            if (data == null || data.optString("name").isEmpty()) continue;
            Receta draft = new Receta();
            draft.setNombre(plainText(data.optString("name")));
            draft.setDescripcion(plainText(data.optString("description")));
            draft.setInstrucciones(instructions(data.opt("recipeInstructions"), 0));
            Object yield = data.opt("recipeYield");
            if (yield instanceof JSONArray) yield = ((JSONArray) yield).opt(0);
            Matcher servings = Pattern.compile("\\d{1,4}").matcher(String.valueOf(yield));
            draft.setPorciones(servings.find() ? Math.max(1, Integer.parseInt(servings.group())) : 1);
            JSONArray ingredients = data.optJSONArray("recipeIngredient");
            if (ingredients != null) {
                for (int index = 0; index < Math.min(ingredients.length(), 100); index++) {
                    String line = plainText(ingredients.optString(index));
                    if (line.isEmpty()) continue;
                    IngredienteReceta ingredient = new IngredienteReceta();
                    Matcher quantity = QUANTITY.matcher(line);
                    ingredient.setOrden(draft.getIngredientes().size());
                    if (quantity.matches()) {
                        ingredient.setCantidad(Float.parseFloat(quantity.group(1).replace(',', '.')));
                        ingredient.setUnidad(quantity.group(2).toLowerCase(java.util.Locale.ROOT));
                        ingredient.setAlimentoNombre(quantity.group(3));
                    } else {
                        ingredient.setAlimentoNombre(line);
                        ingredient.setUnidad("g");
                    }
                    draft.addIngrediente(ingredient);
                }
            }
            return draft;
        }
        throw new IOException("Recipe data missing");
    }

    private JSONObject findRecipe(Object node, int depth) {
        if (depth > 15) return null;
        if (node instanceof JSONObject) {
            JSONObject item = (JSONObject) node;
            Object type = item.opt("@type");
            if ("Recipe".equals(type)) return item;
            if (type instanceof JSONArray) {
                JSONArray types = (JSONArray) type;
                for (int i = 0; i < types.length(); i++) {
                    if ("Recipe".equals(types.optString(i))) return item;
                }
            }
            return findRecipe(item.opt("@graph"), depth + 1);
        }
        if (node instanceof JSONArray) {
            JSONArray nodes = (JSONArray) node;
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject recipe = findRecipe(nodes.opt(i), depth + 1);
                if (recipe != null) return recipe;
            }
        }
        return null;
    }

    private String instructions(Object node, int depth) {
        if (depth > 12) return "";
        if (node instanceof String) return plainText((String) node);
        if (node instanceof JSONObject) {
            JSONObject step = (JSONObject) node;
            return step.has("text") ? plainText(step.optString("text"))
                    : instructions(step.opt("itemListElement"), depth + 1);
        }
        if (node instanceof JSONArray) {
            JSONArray steps = (JSONArray) node;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < steps.length(); i++) {
                String step = instructions(steps.opt(i), depth + 1);
                if (!step.isEmpty()) {
                    if (result.length() > 0) result.append('\n');
                    result.append(step);
                }
            }
            return result.toString();
        }
        return "";
    }

    private String plainText(String text) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        worker.shutdownNow();
    }
}
