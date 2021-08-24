package com.easy.wallet.data.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.easy.framework.common.TimeUtils
import com.easy.wallet.data.data.remote.EtherTransactionInfo
import com.easy.wallet.data.data.remote.blockchair.btc.BitcoinTxRelatedInfo
import com.easy.wallet.data.data.remote.cosmos.TransactionResponse
import kotlinx.parcelize.Parcelize
import okhttp3.internal.toLongOrDefault
import java.math.BigDecimal

@Keep
@Parcelize
data class TransactionDataModel(
  val txHash: String,
  val recipient: String,
  val sender: String,
  val amount: BigDecimal,
  val time: String,
  val direction: TxDirection,
  val status: TxStatus,
  val decimal: Int,
  val symbol: String,
  val fee: String
) : Parcelable {
  companion object {
    internal fun ofCosmos(
      it: TransactionResponse,
      decimal: Int,
      address: String,
      symbol: String
    ): TransactionDataModel {
      val fee = it.txFees.map { it.text.toLongOrDefault(0L) }.let {
        if (it.isEmpty()) 0L
        else it.reduce { acc, value -> acc + value }
      }
      val feeSymbol = it.txFees.firstOrNull()?.currency.orEmpty()
      return TransactionDataModel(
        txHash = it.hash,
        recipient = "",
        amount = BigDecimal.ZERO,
        time = TimeUtils.ISOFormatter(it.time),
        direction = TxDirection.SEND,
        status = TxStatus.PENDING,
        decimal = decimal,
        symbol = symbol,
        sender = address,
        fee = "$fee $feeSymbol"
      )
    }

    internal fun ofEthereumType(
      it: EtherTransactionInfo,
      symbol: String,
      decimal: Int,
      isSend: Boolean
    ): TransactionDataModel {
      val fee = it.cumulativeGasUsed.toBigDecimalOrNull()
        ?.times(it.gasPrice.toBigDecimalOrNull() ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
      return TransactionDataModel(
        txHash = it.hash,
        time = TimeUtils.timestampToString(it.timeStamp.toLongOrNull() ?: 0L),
        amount = it.value.toBigDecimalOrNull() ?: BigDecimal.ZERO,
        recipient = it.to,
        sender = it.from,
        status = TxStatus.CONFIRM,
        direction = if (isSend) TxDirection.SEND else TxDirection.RECEIVE,
        decimal = decimal,
        symbol = symbol,
        fee = "$fee ETH",
      )
    }

    internal fun ofBitcoinType(
      it: BitcoinTxRelatedInfo,
      address: String,
      symbol: String,
      decimal: Int,
      isSend: Boolean
    ): TransactionDataModel {
      val amount = if (isSend) {
        val totalInput =
          it.inputs.map { it.value.toLongOrDefault(0L) }
            .reduce { acc, value -> acc + value }
        val backOutput = it.outputs.filter { it.recipient == address }
          .let { it ->
            if (it.isEmpty()) 0L
            else it.map { it.value.toLongOrDefault(0L) }
              .reduce { acc, value -> acc + value }
          }
        totalInput - backOutput - it.transaction.fee
      } else {
        it.outputs.filter { it.recipient == address }.let { it ->
          if (it.isEmpty()) 0L
          else it.map { it.value.toLongOrDefault(0L) }
            .reduce { acc, value -> acc + value }
        }
      }
      val fee = it.transaction.fee

      return TransactionDataModel(
        txHash = it.transaction.hash,
        time = it.transaction.time,
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
        amount = amount.toBigDecimal(),
        direction = if (isSend) TxDirection.SEND else TxDirection.RECEIVE,
        status = when {
          (it.transaction.blockId == -1L) and it.transaction.hash.isNotBlank() -> TxStatus.PENDING
          (it.transaction.blockId > 0L) and it.transaction.hash.isNotBlank() -> TxStatus.CONFIRM
          else -> TxStatus.FAILURE
        },
        decimal = decimal,
        symbol = symbol,
        fee = "$fee"
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
