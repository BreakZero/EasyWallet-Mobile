package com.easy.wallet.data.data.remote.solana


import com.squareup.moshi.Json

data class SOLBroadcastResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
    val result: String
)