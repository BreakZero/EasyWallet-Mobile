package com.easy.version.dependencies

object AndroidX {
    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val startup = "androidx.startup:startup-runtime:1.0.0"
    const val activity = "androidx.activity:activity:1.2.0-beta01"
    const val webkit = "androidx.webkit:webkit:1.4.0"

    const val securityCrypto = "androidx.security:security-crypto:1.1.0-alpha02"

    const val paging = "androidx.paging:paging-runtime:3.0.0-alpha06"
    const val biometric = "androidx.biometric:biometric:1.2.0-alpha01"

    object Fragment {
        private const val fragment_version = "1.3.0-beta01"
        const val fragment = "androidx.fragment:fragment:$fragment_version"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragment_version"
        const val fragmentTesting = "androidx.fragment:fragment-testing:$fragment_version"
    }

    object Lifecycle {
        private const val lifecycle_version = "2.3.0-beta01"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val viewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
        const val service = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
    }

    object Navigation {
        private const val navigation_version = "2.3.1"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$navigation_version"
    }

    object Room {
        private const val room_version = "2.2.5"
        const val roomRuntime = "androidx.room:room-runtime:$room_version"
        const val roomCompiler = "androidx.room:room-compiler:$room_version"
        const val roomKtx = "androidx.room:room-ktx:$room_version"
    }

    object Camera {
        private const val camera_version = "1.0.0-beta12"
        const val camera = "androidx.camera:camera-camera2:$camera_version"
        const val cameraLife = "androidx.camera:camera-lifecycle:$camera_version"
        const val cameraView = "androidx.camera:camera-view:1.0.0-alpha19"
    }
}