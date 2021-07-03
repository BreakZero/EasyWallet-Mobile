package com.easy.wallet.data.data.remote.blockchair.ethereum

import com.dougie.wallet.data.data.remote.CoinContext
import com.squareup.moshi.Json

internal data class EthereumTxResponse(
    @field:Json(name = "data")
    val data: Map<String, EthereumTxLayer>,
    @field:Json(name = "context")
    val context: CoinContext
)

internal data class EthereumTxLayer(
    @field:Json(name = "transaction")
    val transaction: EthereumTxInfo
)

internal data class EthereumTxInfo(
    @field:Json(name = "block_id")
    val blockId: Long,
    @field:Json(name = "call_count")
    val callCount: Int?,
    @field:Json(name = "date")
    val date: String,
    @field:Json(name = "failed")
    val failed: Boolean?,
    @field:Json(name = "fee")
    val fee: String?,
    @field:Json(name = "fee_usd")
    val feeUsd: Double?,
    @field:Json(name = "gas_limit")
    val gasLimit: Int,
    @field:Json(name = "gas_price")
    val gasPrice: Long,
    @field:Json(name = "gas_used")
    val gasUsed: Int?,
    @field:Json(name = "hash")
    val hash: String,
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "index")
    val index: Int?,
    @field:Json(name = "input_hex")
    val inputHex: String,
    @field:Json(name = "internal_value")
    val internalValue: String?,
    @field:Json(name = "internal_value_usd")
    val internalValueUsd: String?,
    @field:Json(name = "nonce")
    val nonce: String,
    @field:Json(name = "r")
    val r: String,
    @field:Json(name = "recipient")
    val recipient: String,
    @field:Json(name = "s")
    val s: String,
    @field:Json(name = "sender")
    val sender: String,
    @field:Json(name = "time")
    val time: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "v")
    val v: String,
    @Json(name = "values")
    val value: String,
    @Json(name = "value_usd")
    val valueUsd: String
)
