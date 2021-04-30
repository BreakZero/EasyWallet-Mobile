import com.dougie.version.BuildConfig
import com.dougie.version.dependencies.AndroidX
import com.dougie.version.dependencies.Other

plugins {
    id("com.android.library")
    kotlin("android")
    id("version-plugin")
    id("org.jmailen.kotlinter")
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

    android.buildFeatures.viewBinding = true
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
    api(AndroidX.coreKtx)
    api(AndroidX.startup)
    api(AndroidX.constraintLayout)
    api(Other.threetenabp)
    api(Other.material)

    api(AndroidX.Fragment.fragment)
    api(AndroidX.Fragment.fragmentKtx)
    api(AndroidX.Navigation.fragmentKtx)
    api(AndroidX.Navigation.uiKtx)

    api(Other.Koin.koinFragment)
    api(Other.Koin.koinExt)
    api(Other.Koin.koinScope)
    api(Other.Koin.koinViewModel)

    api(AndroidX.Lifecycle.liveDataKtx)
    api(AndroidX.Lifecycle.viewModelKtx)
    api(AndroidX.Lifecycle.commonJava8)
    api(AndroidX.Lifecycle.viewModelSavedState)

    api(Other.timber)

    api(Other.Retrofit.retrofit)
    api(Other.Retrofit.moshi)
    api(Other.okHttpLogger)
    api(Other.coroutineCore)
    api(Other.coroutineAndroid)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}