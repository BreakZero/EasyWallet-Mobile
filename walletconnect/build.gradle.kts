plugins {
    id("com.android.library")
    kotlin("android")
    id("version-plugin")
    id("org.jmailen.kotlinter")
}

android {
    compileSdkVersion(com.dougie.version.BuildConfig.compileSdkVersion)
    buildToolsVersion(com.dougie.version.BuildConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(com.dougie.version.BuildConfig.minSdkVersion)
        targetSdkVersion(com.dougie.version.BuildConfig.targetSdkVersion)
        versionCode = com.dougie.version.BuildConfig.versionCode
        versionName = com.dougie.version.BuildConfig.versionName

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = arrayOf("no-wildcard-imports")
}

dependencies {
    implementation(kotlin("stdlib-jdk8", com.dougie.version.BuildConfig.kotlinVersion))
    api(project(":framework-core"))

    api("com.google.code.gson:gson:2.8.6")
    api("com.github.salomonbrys.kotson:kotson:2.5.0")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

}