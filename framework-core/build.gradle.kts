import com.easy.version.BuildConfig
import com.easy.version.dependencies.AndroidX
import com.easy.version.dependencies.Other

plugins {
    id("com.android.library")
    kotlin("android")
    id("version-plugin")
}

android {
    compileSdk = BuildConfig.compileSdkVersion
    buildToolsVersion = BuildConfig.buildToolsVersion

    defaultConfig {
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion

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

    android.buildFeatures.viewBinding = true
}

dependencies {
    implementation(kotlin("stdlib-jdk8", BuildConfig.kotlinVersion))
    api(AndroidX.coreKtx)
    api(AndroidX.startup)
    api(AndroidX.constraintLayout)
    api(Other.threetenabp)
    api(Other.material)

    api(AndroidX.Fragment.fragment)
    api(AndroidX.Fragment.fragmentKtx)
    api(AndroidX.Navigation.fragmentKtx)
    api(AndroidX.Navigation.uiKtx)

    api(Other.Koin.koinAndroid)

    api(AndroidX.Lifecycle.liveDataKtx)
    api(AndroidX.Lifecycle.viewModelKtx)
    api(AndroidX.Lifecycle.commonJava8)
    api(AndroidX.Lifecycle.viewModelSavedState)

    api(Other.timber)

    api(Other.Retrofit.retrofit)
    api(Other.Retrofit.moshi)
    api(Other.Retrofit.moshiConvert)
    api(Other.okHttpLogger)
    api(Other.coroutineCore)
    api(Other.coroutineAndroid)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}