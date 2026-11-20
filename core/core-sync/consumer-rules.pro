# Consumer ProGuard rules for core-sync

# Keep WorkManager Workers
-keep class com.lupefit.core.sync.worker.** { *; }

# Keep Room entities and DAOs
-keep class com.lupefit.core.sync.model.** { *; }
-keep class com.lupefit.core.sync.queue.** { *; }

# Keep conflict resolution
-keep class com.lupefit.core.sync.conflict.** { *; }

# WorkManager
-keep class androidx.work.** { *; }
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