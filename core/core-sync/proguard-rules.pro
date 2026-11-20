# ProGuard rules for core-sync library

# WorkManager
-keep class androidx.work.** { *; }
-keep interface androidx.work.** { *; }
-dontwarn androidx.work.**

# Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# RxJava3
-keep class io.reactivex.rxjava3.** { *; }
-dontwarn io.reactivex.rxjava3.**

# Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Keep annotations
-keepattributes *Annotation*, EnclosingMethod, InnerClasses, Signature