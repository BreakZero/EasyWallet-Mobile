import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.dougie.version.BuildConfig
import com.dougie.version.dependencies.Other
import com.dougie.version.dependencies.Special

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.squareup.sqldelight")
    id("version-plugin")
    id("kotlin-parcelize")
    id("org.jmailen.kotlinter")
}

sqldelight {
    database("WalletDatabase") {
        packageName = "com.dougie.wallet"
    }
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)
    buildToolsVersion(BuildConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        val infuraApiKey = gradleLocalProperties(rootDir).getProperty("apikey.infura")
        val etherscanApiKey = gradleLocalProperties(rootDir).getProperty("apikey.etherscan")
        val figmentApiKey = gradleLocalProperties(rootDir).getProperty("apikey.figment")
        val bscscanApikey = gradleLocalProperties(rootDir).getProperty("apikey.bscscan")
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "INFURA_APIKEY", infuraApiKey)
            buildConfigField("String", "ETHERSCAN_APIKEY", etherscanApiKey)
            buildConfigField("String", "FIGMENT_APIKEY", figmentApiKey)
            buildConfigField("String", "BSCSCAN_APIKEY", bscscanApikey)
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "INFURA_APIKEY", infuraApiKey)
            buildConfigField("String", "ETHERSCAN_APIKEY", etherscanApiKey)
            buildConfigField("String", "FIGMENT_APIKEY", figmentApiKey)
            buildConfigField("String", "BSCSCAN_APIKEY", bscscanApikey)
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

    implementation(kotlin("stdlib-jdk8", BuildConfig.kotlinVersion))

    api(project(":framework-core"))
    api("com.trustwallet:wallet-core:2.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.1.0")

    implementation(Other.SQLDelight.sqlDelight)
    implementation(Other.SQLDelight.sqlCoroutine)
    api(Special.Web3J)
    api(Special.ERC20)

    testImplementation("junit:junit:4.13")
    androidTestImplementation("io.mockk:mockk-android:1.10.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}