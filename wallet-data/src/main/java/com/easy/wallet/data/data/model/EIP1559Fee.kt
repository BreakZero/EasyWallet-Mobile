package com.easy.wallet.data.data.model

import com.squareup.moshi.Json

internal data class EIP1559Fee(
  @field:Json(name = "baseFee")
  val baseFee: String,
  @field:Json(name = "maxPriorityFee")
  val maxPriorityFee: String
)
