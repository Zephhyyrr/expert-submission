# Keep model classes
-keep class com.firman.core.domain.model.** { *; }
-keep class com.firman.core.data.source.remote.response.** { *; }

# Keep Retrofit service interfaces
-keep,allowobfuscation interface com.firman.core.data.source.remote.ApiService

# Retrofit rules
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }

# Exposed APIs for the app module
-keep class com.firman.core.domain.usecase.** { *; }
-keep class com.firman.core.domain.repository.** { *; }

-keep class com.firman.core.data.Resource { *; }
-keep class com.firman.core.data.Resource$* { *; }


# Keep Kotlin standard library classes
-keep class kotlin.collections.** { *; }
-keep class kotlin.** { *; }

# Keep Koin classes
-keep class org.koin.** { *; }

# Keep all classes in your favorite module
-keep class com.firman.favorite.** { *; }

# If you're using ViewBinding
-keep class com.firman.favorite.databinding.** { *; }

# Keep your ViewModel classes
-keep class com.firman.favorite.ui.** { *; }
-keep class com.firman.favorite.viewmodel.** { *; }

-keep class com.firman.bookapp.ui.** { *; }
