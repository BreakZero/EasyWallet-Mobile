package com.easy.wallet.data.data.remote.solana

import com.squareup.moshi.Json

internal data class SOLBalanceResult(
  @field:Json(name = "context")
  val context: SolBaseContext,
  @field:Json(name = "value")
  val value: String
)
