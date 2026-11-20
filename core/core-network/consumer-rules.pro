# Consumer ProGuard rules for core-network
# These rules are applied to modules that depend on this library

# Keep Retrofit interfaces
-keep interface com.lupefit.core.network.api.** { *; }

# Keep Moshi models
-keep class com.lupefit.core.network.dto.** { *; }
-keep class com.lupefit.core.network.model.** { *; }

# Keep OkHttp interceptors
-keep class com.lupefit.core.network.interceptor.** { *; }

# Moshi
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-keep class okio.** { *; }
-dontwarn okio.**