plugins {
    id("com.android.library")
    kotlin("android")
    id("version-plugin")
}

android {
    compileSdk = com.easy.version.BuildConfig.compileSdkVersion
    buildToolsVersion = com.easy.version.BuildConfig.buildToolsVersion

    defaultConfig {
        minSdk = com.easy.version.BuildConfig.minSdkVersion
        targetSdk = com.easy.version.BuildConfig.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", com.easy.version.BuildConfig.kotlinVersion))
    api(project(":framework-core"))

    api("com.google.code.gson:gson:2.8.6")
    api("com.github.salomonbrys.kotson:kotson:2.5.0")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

}