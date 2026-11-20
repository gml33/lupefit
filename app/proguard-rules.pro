# ProGuard rules for LuPeFit

# Keep Room entities and DAOs
-keep class com.lupefit.app.data.local.entity.** { *; }
-keep class com.lupefit.app.data.local.dao.** { *; }
-keep class com.lupefit.app.data.local.AppDatabase { *; }

# Keep ViewBinding generated classes
-keep class *Binding { *; }

# Keep Material Components
-keep class com.google.android.material.** { *; }

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Keep CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

# Keep Lottie
-keep class com.airbnb.lottie.** { *; }

# Keep Room compiler generated classes
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.Dao { *; }
-keep class * extends androidx.room.Entity { *; }

# Keep Health Connect
-keep class androidx.health.connect.client.** { *; }

# Keep WorkManager
-keep class androidx.work.** { *; }

# Keep Play Services Location
-keep class com.google.android.gms.location.** { *; }

# Keep Kotlin coroutines (used by Room KTX)
-keep class kotlinx.coroutines.** { *; }

# Keep Moshi (if used for JSON)
-keep class com.squareup.moshi.** { *; }

# Keep Guava ListenableFuture
-keep class com.google.common.util.concurrent.** { *; }

# Keep Room KTX coroutines
-keep class androidx.room.coroutines.** { *; }

# Keep annotation processor generated code
-keep @interface androidx.room.*
-keep @interface androidx.room.Database
-keep @interface androidx.room.Entity
-keep @interface androidx.room.Dao
-keep @interface androidx.room.PrimaryKey
-keep @interface androidx.room.ColumnInfo
-keep @interface androidx.room.ForeignKey
-keep @interface androidx.room.Index
-keep @interface androidx.room.TypeConverters

# Don't warn about missing classes
-dontwarn com.google.android.material.**
-dontwarn com.github.mikephil.charting.**
-dontwarn de.hdodenhof.circleimageview.**
-dontwarn com.airbnb.lottie.**
-dontwarn androidx.health.connect.client.**
-dontwarn com.google.android.gms.location.**