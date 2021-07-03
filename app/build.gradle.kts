import com.dougie.version.BuildConfig
import com.dougie.version.dependencies.AndroidX
import com.dougie.version.dependencies.Other
import com.dougie.version.keyStoreProperties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.squareup.sqldelight")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jmailen.kotlinter")
    id("kotlin-parcelize")
    id("version-plugin")
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)
    buildToolsVersion(BuildConfig.buildToolsVersion)

    defaultConfig {
        applicationId = "com.dougie.wallet"
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        setProperty("archivesBaseName", applicationId + versionName)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keyProperties = keyStoreProperties()
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
        create("release") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    android.buildFeatures.viewBinding= true
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

    implementation(project(":framework-core"))
    implementation(project(":wallet-data"))
    implementation(project(":walletconnect"))

    implementation(AndroidX.paging)
    implementation(AndroidX.biometric)

    implementation(AndroidX.webkit)

    implementation(Other.button)

    implementation(Other.Coil.coil)
    implementation(Other.Coil.coilGif)
    implementation(Other.zxing)
    implementation(Other.zxingEmbedded)
    implementation(Other.Epoxy.epoxy)
    implementation(Other.Epoxy.epoxyPaging)
    implementation(Other.Exoplayer.exoplayerCore)
    implementation(Other.Exoplayer.exoplayerUI)

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation(Other.SQLDelight.sqlDelight)
    implementation(Other.SQLDelight.sqlCoroutine)

    implementation(Other.flexbox)
    implementation(AndroidX.securityCrypto)
    kapt(Other.Epoxy.epoxyProcessor)

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}