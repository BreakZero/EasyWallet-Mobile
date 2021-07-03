package com.easy.wallet.data.network.polkadot

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object PolkadotClient {
    private const val BASE_URL = "https://rpc.polkadot.io/"
    private const val TAG = "Polkadot Tag"
    private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.tag(TAG).d(message)
                    }
                }
            ).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }

    fun client(): Retrofit {
        return Retrofit.Builder()
            .client(builder.build())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
