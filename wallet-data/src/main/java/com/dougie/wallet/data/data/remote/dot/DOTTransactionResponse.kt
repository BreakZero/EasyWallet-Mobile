package com.dougie.wallet.data.data.remote.dot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class DOTTransactionResponse(
    @field:Json(name = "code")
    val code: Int,
    @field:Json(name = "data")
    val result: DOTTransactionData,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "ttl")
    val ttl: Int
)

@JsonClass(generateAdapter = true)
data class Extrinsic(
    @field:Json(name = "block_num")
    val blockNum: Int,
    @field:Json(name = "block_timestamp")
    val blockTimestamp: Int,
    @field:Json(name = "call_module")
    val callModule: String,
    @field:Json(name = "call_module_function")
    val callModuleFunction: String,
    @field:Json(name = "extrinsic_hash")
    val extrinsicHash: String,
    @field:Json(name = "extrinsic_index")
    val extrinsicIndex: String,
    @field:Json(name = "fee")
    val fee: String,
    @field:Json(name = "finalized")
    val finalized: Boolean,
    @field:Json(name = "from")
    val from: String,
    @field:Json(name = "params")
    val params: List<Param>,
    @field:Json(name = "success")
    val success: Boolean
)

data class DOTTransactionData(
    @field:Json(name = "count")
    val count: Int,
    @field:Json(name = "extrinsics")
    val extrinsics: List<Extrinsic>
)

data class Param(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "value")
    val value: Any
)
