# Keep app classes
-keep class com.firman.bookapp.** { *; }
-keep class com.firman.core.data.** { *; }
-keep class com.firman.core.di.** { *; }
-keep class com.firman.favorite.di.** { *; }

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

# Keep Resource class and its subclasses
-keep class com.firman.core.data.Resource { *; }
-keep class com.firman.core.data.Resource$* { *; }
-keep class com.firman.core.domain.model.** { *; }

# Play Core Library
-keep class com.google.android.play.core.** { *; }
-keep interface com.google.android.play.core.** { *; }

# Google Play Feature Delivery
-keep class com.google.android.play.feature.** { *; }
-keep interface com.google.android.play.feature.** { *; }

# Keep domain layer classes
-keep class com.firman.core.domain.usecase.** { *; }
-keep class com.firman.core.domain.repository.** { *; }

# ViewBinding (sesuai dengan build.gradle)
-keep class * implements androidx.viewbinding.ViewBinding {
    public static ** bind(android.view.View);
    public static ** inflate(...);
}
-keep class **.*Binding { *; }

# LiveData, ViewModel, Lifecycle
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
    public <fields>;
    public <methods>;
}
-keepclassmembers class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
    public <fields>;
    public <methods>;
}
-keep class androidx.lifecycle.LiveData { *; }
-keep class androidx.lifecycle.MutableLiveData { *; }
-keep class androidx.lifecycle.ViewModelProvider$* { *; }
-keep class androidx.lifecycle.viewmodel.CreationExtras { *; }

# RecyclerView Adapters
-keep class * extends androidx.recyclerview.widget.RecyclerView$ViewHolder { *; }
-keep class * extends androidx.recyclerview.widget.RecyclerView$Adapter { *; }

# Kotlin Coroutines & Flow
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.flow.** { *; }
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlin.coroutines.* {
    public <methods>;
}

# ViewModels in your app specifically
-keep class com.firman.bookapp.ui.** { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# LeakCanary - debug only
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.leakcanary.** { *; }

# Dynamic Feature Modules
-keep class androidx.navigation.dynamicfeatures.fragment.** { *; }

# Keep property names for all ViewModels in XML
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <fields>;
    public <methods>;
}

-keep class com.firman.bookapp.ui.** { *; }
