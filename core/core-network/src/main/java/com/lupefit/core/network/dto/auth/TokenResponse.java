package com.lupefit.core.network.dto.auth;

import com.squareup.moshi.Json;

/** Respuesta de refresh token */
public final class TokenResponse {
    @Json(name = "access_token")
    public final String accessToken;

    @Json(name = "token_type")
    public final String tokenType;

    @Json(name = "expires_in")
    public final int expiresIn;

    public TokenResponse(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}