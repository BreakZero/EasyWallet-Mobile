package com.easy.wallet.data.network.terra

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object TerraClient {
  private const val BASE_URL = "https://bombay-lcd.terra.dev/"
  private const val TAG = "Terra Tag"
  private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
    addInterceptor(
      HttpLoggingInterceptor { message -> Timber.tag(TAG).d(message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
      }
    )
  }

  fun client(): Retrofit {
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
    return Retrofit.Builder()
      .client(builder.build())
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
  }
}
