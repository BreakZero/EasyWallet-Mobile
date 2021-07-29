package com.easy.wallet.data.data.remote.solana

import com.squareup.moshi.Json

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
