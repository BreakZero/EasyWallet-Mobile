package com.easy.wallet.data.data.remote.solana

import com.squareup.moshi.Json

internal data class RecentBlockHashResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
    val result: RecentBlockHashResult
)

internal data class RecentBlockHashResult(
    @field:Json(name = "context")
    val context: SolBaseContext,
    @field:Json(name = "value")
    val value: RecentBlockHashValue
)

internal data class RecentBlockHashValue(
    @field:Json(name = "blockhash")
    val blockhash: String,
    @field:Json(name = "feeCalculator")
    val feeCalculator: FeeCalculator
)

internal data class FeeCalculator(
    @field:Json(name = "lamportsPerSignature")
    val lamportsPerSignature: Int
)
