plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

val compileSdkConf : String by project
val minSdkConf : String by project
val targetSdkConf : String by project
val versionConf: String by project

android {
    namespace = "per.pslilysm.sdk_library"
    compileSdk = compileSdkConf.toInt()

    defaultConfig {
        minSdk = minSdkConf.toInt()
        testOptions.targetSdk = targetSdkConf.toInt()
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

    // https://mvnrepository.com/artifact/org.apache.commons/commons-compress
    compileOnly("org.apache.commons:commons-compress:1.24.0")

    // https://mvnrepository.com/artifact/org.tukaani/xz
    compileOnly("org.tukaani:xz:1.9")

    // retrofit
    compileOnly("com.squareup.retrofit2:retrofit:2.9.0")

    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    compileOnly("commons-codec:commons-codec:1.16.0")

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.pslilysm"
            artifactId = "sdk-ktx"
            version = versionConf
        }
    }
}