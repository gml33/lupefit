# Consumer ProGuard rules for LuPeFit Core UI library
# These rules are applied to projects that depend on this library

# Keep public API
-keep public class com.lupefit.core.ui.** { *; }

# Keep ViewBinding generated classes
-keep class *Binding { *; }

# Keep Material Components styles
-keep class com.google.android.material.** { *; }