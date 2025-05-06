# Keep model classes
-keep class com.firman.core.domain.model.** { *; }
-keep class com.firman.core.data.source.remote.** { *; }
-keep class com.firman.core.data.source.local.entity.** { *; }
-keep class com.firman.core.domain.usecase.** { *; }
-keep class com.firman.core.domain.repository.** { *; }
# Keep Retrofit service interfaces
-keep,allowobfuscation interface com.firman.core.data.source.remote.ApiService

# Retrofit rules
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**

# OkHttp rules
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

-keep class com.firman.core.data.Resource { *; }
-keep class com.firman.core.data.Resource$* { *; }
# Androidx Security
-keep class androidx.security.crypto.** { *; }

# SecurePreferences
-keep class com.securepreferences.** { *; }

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }

# Keep Koin modules
-keep class com.firman.core.di.** { *; }