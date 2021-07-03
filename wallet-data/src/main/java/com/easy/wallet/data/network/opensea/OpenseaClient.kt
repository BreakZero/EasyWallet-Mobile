package com.easy.wallet.data.network.opensea

import com.easy.wallet.data.constant.ChainId
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object OpenseaClient {
    private const val MAINNET_BASE_URL = "https://api.opensea.io/api/v1/"
    private const val RINKEBY_BASE_URL = "https://rinkeby-api.opensea.io/api/v1/"
    private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
            ).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }

    fun client(chainId: ChainId): Retrofit {
        return Retrofit.Builder()
            .client(builder.build())
            .baseUrl(
                when (chainId) {
                    ChainId.MAINNET -> MAINNET_BASE_URL
                    else -> RINKEBY_BASE_URL
                }
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
