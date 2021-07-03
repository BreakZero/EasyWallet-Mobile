package com.easy.wallet.data.data.model

import com.easy.framework.common.TimeUtils
import com.easy.wallet.data.data.remote.EtherTransactionInfo
import com.easy.wallet.data.data.remote.blockchair.btc.BitcoinTxRelatedInfo
import com.easy.wallet.data.data.remote.cosmos.TransactionResponse
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
) {
    companion object {
        internal fun ofCosmos(
            it: TransactionResponse,
            decimal: Int,
            address: String,
            symbol: String
        ): TransactionDataModel {
            return TransactionDataModel(
                txHash = it.hash,
                recipient = "",
                amount = BigDecimal.ZERO,
                time = TimeUtils.ISOFormatter(it.time),
                direction = TxDirection.SEND,
                status = TxStatus.PENDING,
                decimal = decimal,
                symbol = symbol,
                sender = address
            )
        }

        internal fun ofEthereumType(
            it: EtherTransactionInfo,
            symbol: String,
            decimal: Int,
            isSend: Boolean
        ): TransactionDataModel {
            return TransactionDataModel(
                txHash = it.hash,
                time = TimeUtils.timestampToString(it.timeStamp.toLongOrNull() ?: 0L),
                amount = it.value.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                recipient = it.to,
                sender = it.from,
                status = TxStatus.CONFIRM,
                direction = if (isSend) TxDirection.SEND else TxDirection.RECEIVE,
                decimal = decimal,
                symbol = symbol
            )
        }

        internal fun ofBitcoinType(
            it: BitcoinTxRelatedInfo,
            address: String,
            symbol: String,
            decimal: Int,
            isSend: Boolean
        ): TransactionDataModel {
            return TransactionDataModel(
                txHash = it.transaction.hash,
                time = TimeUtils.timestampToString(it.transaction.time.toLongOrNull() ?: 0L),
                recipient = if (isSend) {
                    it.outputs.find {
                        address.equals(it.recipient, true).not()
                    }?.recipient?.ifBlank { "" }.orEmpty()
                } else address,
                sender = if (isSend) {
                    address
                } else {
                    it.outputs.find {
                        address.equals(it.recipient, true).not()
                    }?.recipient?.ifBlank { "" }.orEmpty()
                },
                amount = it.transaction.outputTotal.toBigDecimalOrNull()
                    ?: BigDecimal.ZERO,
                direction = if (isSend) TxDirection.SEND else TxDirection.RECEIVE,
                status = when {
                    (it.transaction.blockId == -1L) and it.transaction.hash.isNotBlank() -> TxStatus.PENDING
                    (it.transaction.blockId > 0L) and it.transaction.hash.isNotBlank() -> TxStatus.CONFIRM
                    else -> TxStatus.FAILURE
                },
                decimal = decimal,
                symbol = symbol
            )
        }
    }
}

enum class TxDirection {
    SEND, RECEIVE
}

enum class TxStatus {
    CONFIRM, FAILURE, PENDING
}
