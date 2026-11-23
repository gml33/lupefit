package com.lupefit.feature.planes.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

final class PlanesStorage {
    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    PlanesStorage(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("planes", Context.MODE_PRIVATE);
    }

    <T> T read(String key, Type type, T fallback) {
        String json = preferences.getString(key, null);
        if (json == null) {
            return fallback;
        }
        try {
            T value = gson.fromJson(json, type);
            return value == null ? fallback : value;
        } catch (JsonParseException e) {
            Log.w("PlanesStorage", "No se pudo leer " + key, e);
            return fallback;
        }
    }

    void write(String key, Object value) {
        preferences.edit().putString(key, gson.toJson(value)).apply();
    }
}
