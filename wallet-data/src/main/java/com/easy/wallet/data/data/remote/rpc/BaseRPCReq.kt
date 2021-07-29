package com.easy.wallet.data.data.remote.rpc

import com.squareup.moshi.Json

data class BaseRPCReq<T>(
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "method")
    val method: String,
    @field:Json(name = "params")
    val params: List<T>
)
