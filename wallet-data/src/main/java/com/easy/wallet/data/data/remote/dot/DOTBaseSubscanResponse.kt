package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

internal data class DOTBaseSubscanResponse<T>(
  @field:Json(name = "code")
  val code: Int,
  @field:Json(name = "data")
  val result: T,
  @field:Json(name = "message")
  val message: String,
  @field:Json(name = "ttl")
  val ttl: Int
)
