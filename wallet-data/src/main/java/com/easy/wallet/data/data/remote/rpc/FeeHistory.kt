package com.easy.wallet.data.data.remote.rpc


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal data class FeeHistory(
    @field:Json(name = "baseFeePerGas")
    val baseFeePerGas: List<String>,
    @field:Json(name = "gasUsedRatio")
    val gasUsedRatio: List<Double>,
    @field:Json(name = "oldestBlock")
    val oldestBlock: String,
    @field:Json(name = "reward")
    val reward: List<List<String>>
)