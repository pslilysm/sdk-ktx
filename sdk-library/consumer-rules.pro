-keepclassmembernames class com.google.gson.JsonObject {
    private final com.google.gson.internal.LinkedTreeMap members;
}
-keepclassmembers,allowobfuscation class * {
  @pers.pslilysm.sdk_library.annotation.GsonExclude <fields>;
}