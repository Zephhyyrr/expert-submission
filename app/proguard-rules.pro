# Keep app classes
-keep class com.firman.bookapp.** { *; }

# Dynamic feature module rules
-keep public class com.google.android.play.core.splitcompat.SplitCompatApplication { *; }
-keep public class * extends com.google.android.play.core.splitcompat.SplitCompatApplication { *; }
-keep class com.google.android.play.core.splitinstall.** { *; }

# Keep Koin for DI
-keep class org.koin.** { *; }
-keep class * extends org.koin.core.module.Module { *; }

# Navigation Component
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class * extends androidx.fragment.app.Fragment{}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.firman.core.data.Resource { *; }
-keep class com.firman.core.data.Resource$* { *; }

# Play Core Library - Updated rules
-keep class com.google.android.play.core.** { *; }
-keep interface com.google.android.play.core.** { *; }

# Google Play Feature Delivery - Updated rules
-keep class com.google.android.play.feature.** { *; }
-keep interface com.google.android.play.feature.** { *; }

-keep class com.firman.core.domain.usecase.** { *; }
-keep class com.firman.core.domain.repository.** { *; }

# Remove the problematic specific rules for SplitInstallState and SplitInstallSessionState
# as they are already covered by the more general rules above