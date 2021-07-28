package com.easy.wallet.data.data.remote.rpc

import com.squareup.moshi.Json

internal data class BalanceReq(
    @field:Json(name = "data")
    val content: String,
    @field:Json(name = "from")
    val from: String,
    @field:Json(name = "to")
    val to: String
)
