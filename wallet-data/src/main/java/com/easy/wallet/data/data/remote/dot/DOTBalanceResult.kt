package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

data class DOTBalanceResponse(
    @field:Json(name = "code")
    val code: Int,
    @field:Json(name = "data")
    val result: DOTBalanceData,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "ttl")
    val ttl: Int
)

data class DOTBalanceData(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "balance")
    val balance: String,
    @field:Json(name = "lock")
    val lock: String
)
