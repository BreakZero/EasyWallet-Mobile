package com.dougie.wallet.data.data.remote.dot

import com.squareup.moshi.Json

data class DOTFeeResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonrpc: String,
    @field:Json(name = "result")
    val result: DOTFeeResult
)

data class DOTFeeResult(
    @field:Json(name = "class")
    val classX: String,
    @field:Json(name = "partialFee")
    val partialFee: String,
    @field:Json(name = "weight")
    val weight: Int
)
