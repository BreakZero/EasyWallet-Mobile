package com.easy.wallet.data.data.remote.blockchair

import com.easy.wallet.data.data.remote.CoinContext
import com.squareup.moshi.Json

internal data class PushTxResponse(
    @field:Json(name = "data")
    val data: TxHashResponse,
    @field:Json(name = "context")
    val context: CoinContext
)

internal data class TxHashResponse(
    @field:Json(name = "transaction_hash")
    val txHash: String
)
