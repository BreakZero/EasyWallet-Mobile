package com.easy.wallet.data.data.model

import java.math.BigDecimal

data class TransactionDataModel(
    val txHash: String,
    val recipient: String,
    val sender: String,
    val amount: BigDecimal,
    val time: String,
    val direction: TxDirection,
    val status: TxStatus,
    val decimal: Int,
    val symbol: String
)

enum class TxDirection {
    SEND, RECEIVE
}

enum class TxStatus {
    CONFIRM, FAILURE, PENDING
}
