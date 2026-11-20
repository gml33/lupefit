package com.lupefit.core.network.dto.auth;

import com.lupefit.core.network.dto.user.UserProfile;
import com.squareup.moshi.Json;

/** Respuesta de autenticación con tokens y perfil */
public final class AuthResponse {
    @Json(name = "access_token")
    public final String accessToken;

    @Json(name = "refresh_token")
    public final String refreshToken;

    @Json(name = "token_type")
    public final String tokenType;

    @Json(name = "expires_in")
    public final int expiresIn;

    @Json(name = "user")
    public final UserProfile user;

    public AuthResponse(String accessToken, String refreshToken,
                       String tokenType, int expiresIn, UserProfile user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }
}