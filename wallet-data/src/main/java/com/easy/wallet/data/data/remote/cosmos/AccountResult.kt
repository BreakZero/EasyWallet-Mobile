package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

internal data class AccountResult(
  @field:Json(name = "type")
  val type: String,
  @field:Json(name = "value")
  val accountValue: AccountValue
)

internal data class AccountValue(
  @field:Json(name = "account_number")
  val accountNumber: String,
  @field:Json(name = "address")
  val address: String,
  @field:Json(name = "coins")
  val coins: List<CosmosCoin>,
  @field:Json(name = "public_key")
  val publicKey: AccountPublicKey,
  @field:Json(name = "sequence")
  val sequence: String
)

internal data class AccountPublicKey(
  @field:Json(name = "type")
  val type: String,
  @field:Json(name = "value")
  val value: String
)
