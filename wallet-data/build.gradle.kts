import com.easy.version.BuildConfig
import com.easy.version.dependencies.Other
import com.easy.version.dependencies.Special
import com.easy.version.keyStoreProperties

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
        packageName = "com.easy.wallet"
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
    val keysProperties = keyStoreProperties()

    buildTypes {
        val infuraApiKey = keysProperties.getProperty("apikey.infura")
        val etherscanApiKey = keysProperties.getProperty("apikey.etherscan")
        val figmentApiKey = keysProperties.getProperty("apikey.figment")
        val bscscanApikey = keysProperties.getProperty("apikey.bscscan")
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
    api(project(":multi-wallet"))
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