plugins {
    id("com.android.library")
    kotlin("android")
    id("version-plugin")
    id("org.jmailen.kotlinter")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = JavaVersion.VERSION_11.toString() }
}

kotlinter {
    ignoreFailures = false
    indentSize = 2
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = arrayOf("no-wildcard-imports")
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