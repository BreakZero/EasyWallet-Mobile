package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

data class TxResult(
  @field:Json(name = "code")
  val code: Int,
  @field:Json(name = "data")
  val dataContent: String,
  @field:Json(name = "gas_used")
  val gasUsed: Int,
  @field:Json(name = "gas_wanted")
  val gasWanted: Int,
  @field:Json(name = "info")
  val info: String,
  @field:Json(name = "log")
  val log: String,
  @field:Json(name = "tags")
  val tags: List<String>
)
