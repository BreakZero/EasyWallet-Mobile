package com.easy.wallet.data.network.cosmos

import com.dougie.wallet.data.constant.APIKey
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object FigmentClient {
    private const val BASE_URL = "https://cosmoshub-4--lcd--full.datahub.figment.io/apikey/${APIKey.FIGMENT_API_KEY}/"
    private const val TAG = "Cosmos Tag"
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
