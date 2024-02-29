# sdk-ktx
A lightweight SDK for Android used to provide utils function and various extension.  
Only support for Kotlin.

## usage

* step 1
```groovy
dependencyResolutionManagement {
    repositories {
        // Add it in your root settings.gradle at the end of repositories:
        maven { url 'https://jitpack.io' }
    }
}
```
    or in settings.gradle.kts
```groovy
import java.net.URI
~~~
dependencyResolutionManagement {
    repositories {
        // Add it in your root settings.gradle.kts at the end of repositories:
        maven { url = URI("https://jitpack.io") }
    }
}
```

* step 2
```groovy
dependencies {
    // add sdk-ktx to your dependencies
    // proguard bundled in aar
    implementation 'com.github.pslilysm:sdk-ktx:2.3.5'
}
```
    or in build.gradle.kts
```groovy
dependencies {
    // add sdk-ktx to your dependencies
    // proguard bundled in aar
    implementation("com.github.pslilysm:sdk-ktx:2.3.5")
}
```



