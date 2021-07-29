package com.easy.wallet.data.data.remote.rpc

import com.squareup.moshi.Json

internal data class BaseRPCResp<T>(
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "result")
    val result: T
)
