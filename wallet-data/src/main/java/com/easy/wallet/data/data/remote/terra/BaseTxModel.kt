package com.easy.wallet.data.data.remote.terra

import com.squareup.moshi.Json

internal data class BaseTxModel(
  @field:Json(name = "tx_bytes")
  val txBytes: String,
  @field:Json(name = "mode")
  val mode: String
)
