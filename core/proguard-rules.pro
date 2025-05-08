# Keep model classes
-keep class com.firman.core.domain.model.** { *; }
-keepclassmembers class com.firman.core.domain.model.** {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.firman.core.domain.model.** {
    <fields>;
}
-keep class com.firman.core.data.source.remote.response.** { *; }

-keep class * {
    @retrofit2.http.* <methods>;
}

# Keep Gson Serialized fields
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep interface retrofit2.** { *; }
-keep class retrofit2.** { *; }
-keepattributes *Annotation*, Signature


# Keep data source classes
-keep class com.firman.core.data.source.remote.** { *; }
-keep class com.firman.core.data.source.local.entity.** { *; }

# Keep domain layer classes
-keep class com.firman.core.domain.usecase.** { *; }
-keep class com.firman.core.domain.repository.** { *; }

# API Service
-keep,allowobfuscation interface com.firman.core.data.source.remote.ApiService
-keepclassmembers interface com.firman.core.data.source.remote.ApiService {
    @retrofit2.http.* <methods>;
}

# Resource class
-keep class com.firman.core.data.Resource { *; }
-keep class com.firman.core.data.Resource$* { *; }

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
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }

# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep class androidx.room.** { *; }

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }

# Androidx Security
-keep class androidx.security.crypto.** { *; }

# SecurePreferences
-keep class com.securepreferences.** { *; }

# Keep Koin modules
-keep class com.firman.core.di.** { *; }

# LiveData and Flow
-keep class androidx.lifecycle.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.flow.** { *; }

# Keep Kotlin Metadata
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions, SourceFile, LineNumberTable, EnclosingMethod
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Keep Enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.firman.bookapp.ui.** { *; }
