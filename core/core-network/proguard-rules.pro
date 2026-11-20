# ProGuard rules for core-network library

# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature, Exceptions
-dontwarn retrofit2.**

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-keep class okio.** { *; }
-dontwarn okio.**

# Moshi
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Guava
-keep class com.google.common.** { *; }
-dontwarn com.google.common.**

# Keep annotations
-keepattributes *Annotation*, EnclosingMethod, InnerClasses, Signature