package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

data class DOTNonceResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
    val result: Int
)
