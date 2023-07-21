# sdk-ktx
A lightweight SDK for Android used to provide utils function and various extension.  
Only support for Kotlin.

## usage

* step 1
```groovy
allprojects {
    repositories {
        // Add it in your root build.gradle at the end of repositories:
        maven { url 'https://jitpack.io' }
    }
}
```
    or in gradle 7 and above
```groovy
dependencyResolutionManagement {
    repositories {
        // Add it in your root build.gradle at the end of repositories:
        maven { url 'https://jitpack.io' }
    }
}
```

* step 2
```groovy
dependencies {
    // add sdk-ktx to your dependencies
    // proguard bundled in aar
    implementation 'com.github.pslilysm:sdk-ktx:2.2.4'
}
```

