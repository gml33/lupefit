# ProGuard rules for LuPeFit Core UI library

# Keep ViewBinding classes
-keep class *Binding { *; }
-keep class *Binding$* { *; }

# Keep DataBinding classes
-keep class *DataBinding { *; }
-keep class *DataBinding$* { *; }

# Keep Material Components
-keep class com.google.android.material.** { *; }

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Keep CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

# Keep Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep serialization
-keep class kotlin.** { *; }