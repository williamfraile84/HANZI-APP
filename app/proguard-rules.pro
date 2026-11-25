# Room Database Rules
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <init>();
}
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keepclassmembers class * {
    @androidx.room.Dao *;
}

# Entidades de Datos y POJOs
-keep class com.jukhg10.hanzimemo.data.models.** { *; }
-keep class com.jukhg10.hanzimemo.data.database.** { *; }

# Google ML Kit OCR
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# Dagger / Hilt
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper
-keepclassmembers,allowobfuscation class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# ANTLR Runtime Rules
-keep class org.antlr.v4.runtime.** { *; }