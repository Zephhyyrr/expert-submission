# Keep model classes
-keep class com.firman.core.domain.model.** { *; }

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
