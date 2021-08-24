import com.easy.version.BuildConfig
import com.easy.version.dependencies.AndroidX
import com.easy.version.dependencies.Other
import com.easy.version.keyStoreProperties

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("com.squareup.sqldelight")
  id("androidx.navigation.safeargs.kotlin")
  id("kotlin-parcelize")
  id("version-plugin")
}

android {
  compileSdk = BuildConfig.compileSdkVersion
  buildToolsVersion = BuildConfig.buildToolsVersion

  val keyProperties = keyStoreProperties()
  defaultConfig {
    applicationId = "com.easy.wallet"
    minSdk = BuildConfig.minSdkVersion
    targetSdk = BuildConfig.targetSdkVersion
    versionCode = BuildConfig.versionCode
    versionName = BuildConfig.versionName

    setProperty("archivesBaseName", applicationId + versionName)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

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
  lint {
    isAbortOnError = false
  }
  android.buildFeatures.viewBinding = true
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
  implementation("com.github.TrustWallet:trust-web3-provider:1.0.4")

  implementation(Other.SQLDelight.sqlDelight)
  implementation(Other.SQLDelight.sqlCoroutine)

  implementation(Other.UniFlow.uniflowCore)
  implementation(Other.UniFlow.uniflowAndroid)

  implementation(Other.flexbox)
  implementation(AndroidX.securityCrypto)
  kapt(Other.Epoxy.epoxyProcessor)

  testImplementation("junit:junit:4.13.1")
  androidTestImplementation("androidx.test.ext:junit:1.1.2")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}