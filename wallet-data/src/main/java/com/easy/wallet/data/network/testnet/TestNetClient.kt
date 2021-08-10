package com.easy.wallet.data.network.testnet

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object TestNetClient {
  private const val BASE_URL = "https://api.bitaps.com/"
  private val builder: OkHttpClient.Builder = OkHttpClient.Builder()

  fun client(): Retrofit {
    builder.addInterceptor(
      HttpLoggingInterceptor(
        object : HttpLoggingInterceptor.Logger {
          override fun log(message: String) {
            Timber.d(message)
          }
        }
      ).apply {
        level = HttpLoggingInterceptor.Level.BASIC
      }
    )
    return Retrofit.Builder()
      .client(builder.build())
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }
}
