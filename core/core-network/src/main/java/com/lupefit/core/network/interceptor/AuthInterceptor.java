package com.lupefit.core.network.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/** Interceptor para agregar token de autorización */
public final class AuthInterceptor implements Interceptor {

    private String accessToken;

    public AuthInterceptor() {}

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void clearAccessToken() {
        this.accessToken = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        
        if (accessToken != null && !accessToken.isEmpty()) {
            Request authenticated = original.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
            return chain.proceed(authenticated);
        }
        
        return chain.proceed(original);
    }
}