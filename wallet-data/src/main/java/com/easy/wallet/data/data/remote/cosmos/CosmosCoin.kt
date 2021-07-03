package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

data class CosmosCoin(
    @Json(name = "amount")
    val amount: String,
    @Json(name = "denom")
    val denom: String
)
