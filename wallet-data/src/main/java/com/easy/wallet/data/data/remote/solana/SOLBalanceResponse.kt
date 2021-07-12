package com.easy.wallet.data.data.remote.solana

import com.squareup.moshi.Json

internal data class SOLBalanceResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
    val result: SOLBalanceResult
)

internal data class SOLBalanceResult(
    @field:Json(name = "context")
    val context: SolBaseContext,
    @field:Json(name = "value")
    val value: String
)
