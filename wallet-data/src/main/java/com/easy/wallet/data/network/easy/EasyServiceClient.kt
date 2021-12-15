package com.easy.wallet.data.network.easy

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object EasyServiceClient {
  private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
    addInterceptor(
      HttpLoggingInterceptor { message -> Timber.tag("HELLO").d(message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
      }
    )
  }

  fun client(): Retrofit {
    val baseUrl = ""
    return Retrofit.Builder()
      .client(builder.build())
      .baseUrl(baseUrl)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }
}
