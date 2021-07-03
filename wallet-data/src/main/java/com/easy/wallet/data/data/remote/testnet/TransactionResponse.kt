package com.easy.wallet.data.data.remote.testnet

import com.squareup.moshi.Json

data class TransactionResponse(
    @field:Json(name = "data")
    val dataInfo: TxDataInfo,
    @field:Json(name = "time")
    val time: Double
)

data class TxDataInfo(
    @field:Json(name = "limit")
    val limit: Int,
    @field:Json(name = "list")
    val transactions: List<TransactionInfo>,
    @field:Json(name = "page")
    val page: Int,
    @field:Json(name = "pages")
    val pages: Int
)

data class TransactionInfo(
    @field:Json(name = "amount")
    val amount: Long,
    @field:Json(name = "blockHeight")
    val blockHeight: Int,
    @field:Json(name = "blockIndex")
    val blockIndex: Int,
    @field:Json(name = "confirmations")
    val confirmations: Int,
    @field:Json(name = "fee")
    val fee: Long,
    @field:Json(name = "firstSeenTimestamp")
    val firstSeenTimestamp: Int,
    @field:Json(name = "from")
    val from: String,
    @field:Json(name = "gasLimit")
    val gasLimit: Int,
    @field:Json(name = "gasPrice")
    val gasPrice: Int,
    @field:Json(name = "gasUsed")
    val gasUsed: Int,
    @field:Json(name = "input")
    val input: String,
    @field:Json(name = "maxFee")
    val maxFee: Long,
    @field:Json(name = "msgError")
    val msgError: String?,
    @field:Json(name = "nonce")
    val nonce: Int,
    @field:Json(name = "receivedAmount")
    val receivedAmount: Long,
    @field:Json(name = "receivedTxCount")
    val receivedTxCount: Int,
    @field:Json(name = "sentAmount")
    val sentAmount: Int,
    @field:Json(name = "sentTxCount")
    val sentTxCount: Int,
    @field:Json(name = "time")
    val time: String,
    @field:Json(name = "timestamp")
    val timestamp: Int,
    @field:Json(name = "to")
    val to: String,
    @field:Json(name = "to_contract")
    val toContract: Boolean,
    @field:Json(name = "txHash")
    val txHash: String,
    @field:Json(name = "type")
    val type: List<String>
) {
    fun isReceive(): Boolean {
        return type.first().equals("received", true)
    }
}
