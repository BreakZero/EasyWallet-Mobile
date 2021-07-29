package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

internal data class DOTBalanceData(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "balance")
    val balance: String,
    @field:Json(name = "lock")
    val lock: String
)
