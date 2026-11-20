package com.lupefit.core.network.dto.auth;

import com.squareup.moshi.Json;

/** Request para login */
public final class LoginRequest {
    @Json(name = "email")
    public final String email;

    @Json(name = "password")
    public final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}