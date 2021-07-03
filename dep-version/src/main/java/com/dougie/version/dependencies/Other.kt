package com.dougie.version.dependencies

object Other {
    const val material = "com.google.android.material:material:1.2.1"
    const val flexbox = "com.google.android:flexbox:2.0.1"
    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1"
    const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1"

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val okHttpLogger = "com.squareup.okhttp3:logging-interceptor:4.1.1"

    const val zxingEmbedded = "com.journeyapps:zxing-android-embedded:4.1.0"
    const val zxing = "com.google.zxing:core:3.4.1"
    const val mlKit = "com.google.mlkit:barcode-scanning:16.1.0"

    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:1.8.0"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:1.8.0"

    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.3.0"

    const val button = "com.github.razir.progressbutton:progressbutton:2.1.0"

    object Coil {
        private const val coil_version = "1.2.0"
        const val coil = "io.coil-kt:coil:$coil_version"
        const val coilGif = "io.coil-kt:coil-gif:$coil_version"
    }

    object SQLDelight {
        private const val sql_version = "1.5.0"
        const val sqlDelight = "com.squareup.sqldelight:android-driver:$sql_version"
        const val sqlCoroutine = "com.squareup.sqldelight:coroutines-extensions-jvm:$sql_version"
    }

    object Epoxy {
        private const val epoxy_version = "4.2.0"
        const val epoxyPaging = "com.airbnb.android:epoxy-paging3:$epoxy_version"
        const val epoxy = "com.airbnb.android:epoxy:$epoxy_version"
        const val epoxyProcessor = "com.airbnb.android:epoxy-processor:$epoxy_version"
    }

    object Retrofit {
        private const val retrofit_version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
        const val moshi = "com.squareup.retrofit2:converter-moshi:$retrofit_version"
    }

    object Koin {
        private const val koin_version = "2.2.0"
        const val koinFragment = "org.koin:koin-androidx-fragment:$koin_version"
        const val koinScope = "org.koin:koin-androidx-scope:$koin_version"
        const val koinViewModel = "org.koin:koin-androidx-viewmodel:$koin_version"
        const val koinExt = "org.koin:koin-androidx-ext:$koin_version"
    }

    object Exoplayer {
        private const val player_version = "2.13.3"
        const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:$player_version"
        const val exoplayerUI = "com.google.android.exoplayer:exoplayer-ui:$player_version"
    }
}