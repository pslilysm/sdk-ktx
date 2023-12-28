plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "per.pslilysm.sdk_library"
    compileSdk = (property("compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (property("minSdk") as String).toInt()
        testOptions.targetSdk = (property("targetSdk") as String).toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    compileOnly("androidx.core:core-ktx:1.12.0")
    compileOnly("androidx.appcompat:appcompat:1.6.1")

    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("com.tencent:mmkv:1.2.16")
    compileOnly("commons-io:commons-io:2.15.1")

    // https://mvnrepository.com/artifact/org.apache.commons/commons-compress
    compileOnly("org.apache.commons:commons-compress:1.21")

    // https://mvnrepository.com/artifact/org.tukaani/xz
    compileOnly("org.tukaani:xz:1.9")

    // retrofit
    compileOnly("com.squareup.retrofit2:retrofit:2.9.0")

    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    compileOnly("commons-codec:commons-codec:1.16.0")

}