package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

internal data class AtomBalance(
  @field:Json(name = "denom")
  val denom: String,
  @field:Json(name = "amount")
  val amount: String
)
