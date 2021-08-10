package com.easy.wallet.data.network.polkadot

import com.squareup.moshi.Json

data class PolkadotRPCRequestBody(
  @field:Json(name = "id")
  val id: Int = 1,
  @field:Json(name = "jsonrpc")
  val jsonrpc: String = "2.0",
  @field:Json(name = "method")
  val methodName: String,
  @field:Json(name = "params")
  val params: List<String>? = null
)
