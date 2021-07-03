package com.easy.wallet.data.data.remote

import com.squareup.moshi.Json

data class EtherScanResponse(
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "result")
    val result: List<EtherTransactionInfo>,
    @field:Json(name = "status")
    val status: String // 1 OK, 0 Error
)

data class EtherTransactionInfo(
    @field:Json(name = "blockHash")
    val blockHash: String,
    @field:Json(name = "blockNumber")
    val blockNumber: String,
    @field:Json(name = "confirmations")
    val confirmations: String,
    @field:Json(name = "contractAddress")
    val contractAddress: String,
    @field:Json(name = "cumulativeGasUsed")
    val cumulativeGasUsed: String,
    @field:Json(name = "from")
    val from: String,
    @field:Json(name = "gas")
    val gas: String,
    @field:Json(name = "gasPrice")
    val gasPrice: String,
    @field:Json(name = "gasUsed")
    val gasUsed: String,
    @field:Json(name = "hash")
    val hash: String,
    @field:Json(name = "input")
    val input: String,
    @field:Json(name = "isError")
    val isError: String,
    @field:Json(name = "nonce")
    val nonce: String,
    @field:Json(name = "timeStamp")
    val timeStamp: String,
    @field:Json(name = "to")
    val to: String,
    @field:Json(name = "transactionIndex")
    val transactionIndex: String,
    @field:Json(name = "txreceipt_status")
    val txreceiptStatus: String,
    @field:Json(name = "value")
    val value: String,

    // for contract
    @field:Json(name = "tokenDecimal")
    val tokenDecimal: String,
    @field:Json(name = "tokenName")
    val tokenName: String,
    @field:Json(name = "tokenSymbol")
    val tokenSymbol: String,
)
