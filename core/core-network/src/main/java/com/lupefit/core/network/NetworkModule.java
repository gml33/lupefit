package com.lupefit.core.network;

import com.lupefit.core.network.api.LuPeFitApiService;
import com.lupefit.core.network.interceptor.AuthInterceptor;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Módulo para configurar Retrofit y API Service
 * Nota: Usamos Moshi en lugar de Gson para mejor performance en Kotlin/Java
 */
public final class NetworkModule {

    private static final String BASE_URL = "https://api.lupefit.com/";
    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;

    private NetworkModule() {}

    /**
     * Crea el cliente OkHttp con interceptores
     */
    public static OkHttpClient createOkHttpClient(AuthInterceptor authInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    }

    /**
     * Configura Moshi (RFC3339 dates handled automatically)
     */
    public static Moshi createMoshi() {
        return new Moshi.Builder()
            .build();
    }

    /**
     * Crea la instancia de Retrofit
     */
    public static Retrofit createRetrofit(OkHttpClient client, Moshi moshi) {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build();
    }

    /**
     * Crea el API Service
     */
    public static LuPeFitApiService createApiService(Retrofit retrofit) {
        return retrofit.create(LuPeFitApiService.class);
    }
}