package com.easy.wallet.data.data.remote.solana

import com.squareup.moshi.Json

internal data class SolBaseContext(
  @field:Json(name = "slot")
  val slot: Int
)
