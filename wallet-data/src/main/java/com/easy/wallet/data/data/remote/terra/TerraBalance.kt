package com.easy.wallet.data.data.remote.terra

import com.squareup.moshi.Json

internal data class TerraBalance(
    @field:Json(name = "balances")
    val balances: List<Balance>,
    @field:Json(name = "pagination")
    val pagination: Pagination
)

internal data class Balance(
    @field:Json(name = "amount")
    val amount: String,
    @field:Json(name = "denom")
    val denom: String
)
