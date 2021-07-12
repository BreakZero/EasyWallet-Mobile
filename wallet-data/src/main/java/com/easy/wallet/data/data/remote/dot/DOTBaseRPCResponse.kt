package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

internal data class DOTBaseRPCResponse<T>(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
    val result: T
)
