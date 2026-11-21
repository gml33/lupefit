package com.lupefit.app.data.local.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.lupefit.app.data.local.dao.FoodProductDao;
import com.lupefit.app.data.local.entity.FoodProduct;
import com.lupefit.core.domain.repository.FoodCatalogRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementación del repositorio del catálogo de alimentos - Offline-First con Open Food Facts.
 * Implementa la interfaz FoodCatalogRepository del core-domain.
 * Flujo:
 * 1. Busca en Room local (instantáneo)
 * 2. Si no existe y hay red → consulta Open Food Facts API
 * 3. Guarda en Room para uso offline futuro
 * 4. Si no hay red o API falla → retorna error (UI manejará fallback)
 */
public class FoodCatalogRepositoryImpl implements FoodCatalogRepository {

    private static final String TAG = "FoodCatalogRepository";
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v0/product/";
    private static final String USER_AGENT = "LuPeFit/1.0 (https://github.com/gml33/lupefit; contact@lupefit.app)";

    private final FoodProductDao productDao;
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final ExecutorService executor;
    private final Context context;

    public FoodCatalogRepositoryImpl(Context context, FoodProductDao productDao) {
        this.context = context.getApplicationContext();
        this.productDao = productDao;
        this.gson = new Gson();

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void searchByName(String query, int limit, OnFoodSearchListener listener) {
        if (query == null || query.trim().isEmpty()) {
            listener.onResult(List.of(), null);
            return;
        }

        // Buscar en local (sync para simplicidad en parser de voz)
        List<FoodProduct> localResults = productDao.searchByNameSync(query.trim(), limit);
        List<com.lupefit.core.domain.model.FoodProduct> domainResults = convertToDomainList(localResults);
        listener.onResult(domainResults, null);
    }

    /**
     * Busca un producto por código de barras.
     * Callback en main thread.
     */
    public void findByBarcode(String barcode, FoodProductCallback callback) {
        if (barcode == null || barcode.trim().isEmpty()) {
            callback.onResult(null);
            return;
        }

        // 1. Buscar en local primero (sync para respuesta instantánea)
        FoodProduct local = productDao.findByBarcodeSync(barcode);
        if (local != null) {
            Log.d(TAG, "Encontrado en local: " + local.name);
            callback.onResult(local);
            return;
        }

        // 2. Si no está en local y hay red, consultar API
        if (isNetworkAvailable()) {
            fetchFromOpenFoodFacts(barcode, callback);
        } else {
            Log.d(TAG, "Sin red y no encontrado en local");
            callback.onResult(null);
        }
    }

    /**
     * Obtiene todos los alimentos frescos precargados.
     */
    @Override
    public void getFreshFoods(OnFreshFoodsListener listener) {
        List<com.lupefit.core.domain.model.FoodProduct> foods = convertToDomainList(productDao.getAllFreshFoodsSync());
        listener.onResult(foods);
    }

    /**
     * Obtiene todos los alimentos frescos precargados (legacy callback).
     */
    public void getFreshFoods(FoodProductListCallback callback) {
        List<FoodProduct> foods = productDao.getAllFreshFoodsSync();
        callback.onResult(foods);
    }

    /**
     * Consulta Open Food Facts API y guarda resultado en Room.
     */
    private void fetchFromOpenFoodFacts(String barcode, FoodProductCallback callback) {
        String url = BASE_URL + barcode + ".json?fields=product_name,brands,nutriments,serving_size";

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG, "Error consultando OpenFoodFacts: " + e.getMessage());
                // En caso de error de red, callback en main thread con null
                executor.execute(() -> callback.onResult(null));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        Log.w(TAG, "OpenFoodFacts HTTP " + response.code());
                        executor.execute(() -> callback.onResult(null));
                        return;
                    }

                    String json = response.body() != null ? response.body().string() : "{}";
                    OpenFoodFactsResponse ofResponse = gson.fromJson(json, OpenFoodFactsResponse.class);

                    if (ofResponse == null || ofResponse.product == null) {
                        Log.d(TAG, "Producto no encontrado en OpenFoodFacts: " + barcode);
                        executor.execute(() -> callback.onResult(null));
                        return;
                    }

                    // Mapear a FoodProduct (Room entity)
                    FoodProduct product = mapToFoodProduct(barcode, ofResponse.product);
                    if (product != null) {
                        // Guardar en Room para uso offline
                        long id = productDao.insert(product);
                        product.id = (int) id;
                        Log.d(TAG, "Guardado desde OpenFoodFacts: " + product.name + " (id=" + id + ")");
                    }

                    executor.execute(() -> callback.onResult(product));

                } catch (Exception e) {
                    Log.e(TAG, "Error parseando respuesta OpenFoodFacts", e);
                    executor.execute(() -> callback.onResult(null));
                }
            }
        });
    }

    /**
     * Mapea respuesta de Open Food Facts a entidad FoodProduct (Room).
     */
    private FoodProduct mapToFoodProduct(String barcode, OpenFoodFactsProduct apiProduct) {
        if (apiProduct.product_name == null || apiProduct.product_name.trim().isEmpty()) {
            return null;
        }

        OpenFoodFactsNutriments n = apiProduct.nutriments;
        if (n == null) {
            return null; // Sin datos nutricionales no sirve
        }

        FoodProduct product = new FoodProduct();
        product.barcode = barcode;
        product.name = apiProduct.product_name.trim();
        product.brand = apiProduct.brands != null ? apiProduct.brands.trim() : "";
        product.kcalPer100g = n.energy_kcal_100g != null ? n.energy_kcal_100g : 0;
        product.proteinPer100g = n.proteins_100g != null ? n.proteins_100g : 0;
        product.carbsPer100g = n.carbohydrates_100g != null ? n.carbohydrates_100g : 0;
        product.fatPer100g = n.fat_100g != null ? n.fat_100g : 0;
        product.defaultServingGrams = parseServingSize(apiProduct.serving_size);
        product.isFreshFood = false;
        product.lastUpdated = System.currentTimeMillis();

        return product;
    }

    /**
     * Convierte lista de Room entities a Domain models.
     */
    private List<com.lupefit.core.domain.model.FoodProduct> convertToDomainList(List<FoodProduct> entities) {
        List<com.lupefit.core.domain.model.FoodProduct> result = new ArrayList<>(entities.size());
        for (FoodProduct entity : entities) {
            result.add(convertToDomain(entity));
        }
        return result;
    }

    /**
     * Convierte Room entity a Domain model.
     */
    private com.lupefit.core.domain.model.FoodProduct convertToDomain(FoodProduct entity) {
        com.lupefit.core.domain.model.FoodProduct domain = new com.lupefit.core.domain.model.FoodProduct();
        domain.setId(String.valueOf(entity.id));
        domain.setBarcode(entity.barcode);
        domain.setName(entity.name);
        domain.setBrand(entity.brand);
        domain.setKcalPer100g(entity.kcalPer100g);
        domain.setProteinPer100g(entity.proteinPer100g);
        domain.setCarbsPer100g(entity.carbsPer100g);
        domain.setFatPer100g(entity.fatPer100g);
        domain.setDefaultServingGrams(entity.defaultServingGrams);
        domain.setFreshFood(entity.isFreshFood);
        domain.setLastUpdated(entity.lastUpdated);
        domain.setReferencePrice(entity.referencePrice);
        domain.setReferenceQuantityGrams(entity.referenceQuantityGrams);
        domain.setPriceUpdatedAt(entity.priceUpdatedAt);
        return domain;
    }

    /**
     * Parsea serving_size (ej: "100g", "200 ml", "1 unidad") a gramos estimados.
     */
    private double parseServingSize(String servingSize) {
        if (servingSize == null) return 100.0;

        String s = servingSize.toLowerCase().trim();
        // Buscar número
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:[.,]\\d+)?)").matcher(s);
        if (m.find()) {
            double value = Double.parseDouble(m.group(1).replace(',', '.'));
            if (s.contains("ml") || s.contains("cl")) {
                return value; // Asumir densidad ~1g/ml
            }
            if (s.contains("kg")) {
                return value * 1000;
            }
            return value; // gramos o unidades
        }
        return 100.0;
    }

    /**
     * Verifica conectividad de red.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return nc != null && (
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        } else {
            android.net.NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
    }

    /**
     * Cierra recursos.
     */
    public void shutdown() {
        executor.shutdown();
        httpClient.dispatcher().executorService().shutdown();
    }

    // ==================== CALLBACKS LOCALES (para compatibilidad) ====================

    public interface FoodProductCallback {
        void onResult(FoodProduct product);
    }

    public interface FoodProductListCallback {
        void onResult(List<FoodProduct> products);
    }

    // ==================== MODELOS RESPUESTA OPEN FOOD FACTS ====================

    private static class OpenFoodFactsResponse {
        @SerializedName("status")
        int status;

        @SerializedName("product")
        OpenFoodFactsProduct product;
    }

    private static class OpenFoodFactsProduct {
        @SerializedName("product_name")
        String product_name;

        @SerializedName("brands")
        String brands;

        @SerializedName("nutriments")
        OpenFoodFactsNutriments nutriments;

        @SerializedName("serving_size")
        String serving_size;
    }

    private static class OpenFoodFactsNutriments {
        @SerializedName("energy-kcal_100g")
        Double energy_kcal_100g;

        @SerializedName("proteins_100g")
        Double proteins_100g;

        @SerializedName("carbohydrates_100g")
        Double carbohydrates_100g;

        @SerializedName("fat_100g")
        Double fat_100g;
    }
}