package com.easy.wallet.data.network.solana

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object SolanaClient {
  private const val BASE_URL = "https://api.testnet.solana.com"
  private const val TAG = "Solana Tag"
  private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
    addInterceptor(
      HttpLoggingInterceptor { message -> Timber.tag(TAG).d(message) }.apply {
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
