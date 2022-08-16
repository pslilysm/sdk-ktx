# sdk-ktx
A lightweight SDK used to assemble mvp and provide utils function
Check out app module to know how to use.

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
    implementation 'com.github.pslilysm:sdk-ktx:2.0.0'
}
```

