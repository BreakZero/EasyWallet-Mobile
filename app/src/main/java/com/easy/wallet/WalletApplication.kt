package com.easy.wallet

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.util.CoilUtils
import com.easy.framework.common.CrashCollection
import com.easy.wallet.koin.appModule
import com.easy.wallet.koin.scopeModule
import com.jakewharton.threetenabp.AndroidThreeTen
import io.uniflow.android.logger.AndroidMessageLogger
import io.uniflow.core.logger.UniFlowLogger
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WalletApplication : Application(), ImageLoaderFactory {
    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        CrashCollection.handleCrash()
        UniFlowLogger.init(AndroidMessageLogger(showDebug = BuildConfig.DEBUG))

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WalletApplication)
            fragmentFactory()
            koin.loadModules(listOf(appModule, scopeModule))
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .componentRegistry {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(this@WalletApplication))
                } else {
                    add(GifDecoder())
                }
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }.build()
    }
}
