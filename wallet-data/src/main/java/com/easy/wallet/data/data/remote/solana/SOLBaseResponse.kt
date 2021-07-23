package com.easy.wallet.data.data.remote.solana

import com.squareup.moshi.Json

internal data class SOLBaseResponse<T>(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
<<<<<<< Updated upstream:wallet-data/src/main/java/com/easy/wallet/data/data/remote/solana/SOLBaseResponse.kt
    val result: T
=======
    val result: String
>>>>>>> Stashed changes:wallet-data/src/main/java/com/easy/wallet/data/data/remote/solana/SOLBroadcastResponse.kt
)
