package com.easy.wallet.data.data.remote.blockchair.btc

import com.easy.wallet.data.data.remote.CoinContext
import com.squareup.moshi.Json

internal data class BitcoinTxResponse(
    @field:Json(name = "data")
    val data: Map<String, BitcoinTxRelatedInfo>,
    @field:Json(name = "context")
    val context: CoinContext
)

internal data class BitcoinTxRelatedInfo(
    @field:Json(name = "inputs")
    val inputs: List<BitcoinInputInfo>,
    @field:Json(name = "outputs")
    val outputs: List<BitcoinOutputInfo>,
    @field:Json(name = "transaction")
    val transaction: BitcoinTxInfo
)

internal data class BitcoinInputInfo(
    @field:Json(name = "block_id")
    val blockId: Long,
    @field:Json(name = "cdd")
    val cdd: String?,
    @field:Json(name = "date")
    val date: String,
    @field:Json(name = "index")
    val index: Int,
    @field:Json(name = "is_from_coinbase")
    val isFromCoinbase: Boolean,
    @field:Json(name = "is_spendable")
    val isSpendable: Any?,
    @field:Json(name = "is_spent")
    val isSpent: Boolean,
    @field:Json(name = "lifespan")
    val lifespan: Int?,
    @field:Json(name = "recipient")
    val recipient: String,
    @field:Json(name = "script_hex")
    val scriptHex: String,
    @field:Json(name = "spending_block_id")
    val spendingBlockId: Long?,
    @field:Json(name = "spending_date")
    val spendingDate: String?,
    @field:Json(name = "spending_index")
    val spendingIndex: Int?,
    @field:Json(name = "spending_sequence")
    val spendingSequence: Long?,
    @field:Json(name = "spending_signature_hex")
    val spendingSignatureHex: String?,
    @field:Json(name = "spending_time")
    val spendingTime: String?,
    @field:Json(name = "spending_transaction_hash")
    val spendingTransactionHash: String?,
    @field:Json(name = "spending_transaction_id")
    val spendingTransactionId: String?,
    @field:Json(name = "spending_value_usd")
    val spendingValueUsd: String?,
    @field:Json(name = "spending_witness")
    val spendingWitness: String?,
    @field:Json(name = "time")
    val time: String,
    @field:Json(name = "transaction_hash")
    val transactionHash: String,
    @field:Json(name = "transaction_id")
    val transactionId: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "values")
    val value: String,
    @field:Json(name = "value_usd")
    val valueUsd: String
)

internal data class BitcoinOutputInfo(
    @field:Json(name = "block_id")
    val blockId: Long,
    @field:Json(name = "cdd")
    val cdd: String?,
    @field:Json(name = "date")
    val date: String,
    @field:Json(name = "index")
    val index: Int,
    @field:Json(name = "is_from_coinbase")
    val isFromCoinbase: Boolean,
    @field:Json(name = "is_spendable")
    val isSpendable: Any?,
    @field:Json(name = "is_spent")
    val isSpent: Boolean,
    @field:Json(name = "lifespan")
    val lifespan: Int?,
    @field:Json(name = "recipient")
    val recipient: String,
    @field:Json(name = "script_hex")
    val scriptHex: String,
    @field:Json(name = "spending_block_id")
    val spendingBlockId: Long?,
    @field:Json(name = "spending_date")
    val spendingDate: String,
    @field:Json(name = "spending_index")
    val spendingIndex: Int?,
    @field:Json(name = "spending_sequence")
    val spendingSequence: Long?,
    @field:Json(name = "spending_signature_hex")
    val spendingSignatureHex: String?,
    @field:Json(name = "spending_time")
    val spendingTime: String?,
    @field:Json(name = "spending_transaction_hash")
    val spendingTransactionHash: String?,
    @field:Json(name = "spending_transaction_id")
    val spendingTransactionId: String?,
    @field:Json(name = "spending_value_usd")
    val spendingValueUsd: String?,
    @field:Json(name = "spending_witness")
    val spendingWitness: String?,
    @field:Json(name = "time")
    val time: String,
    @field:Json(name = "transaction_hash")
    val transactionHash: String,
    @field:Json(name = "transaction_id")
    val transactionId: String?,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "values")
    val value: String,
    @field:Json(name = "value_usd")
    val valueUsd: String
)

internal data class BitcoinTxInfo(
    @field:Json(name = "block_id")
    val blockId: Long,
    @field:Json(name = "cdd_total")
    val cddTotal: String,
    @field:Json(name = "date")
    val date: String,
    @field:Json(name = "fee")
    val fee: Int,
    @field:Json(name = "fee_per_kb")
    val feePerKb: String,
    @field:Json(name = "fee_per_kb_usd")
    val feePerKbUsd: String,
    @field:Json(name = "fee_per_kwu")
    val feePerKwu: String,
    @field:Json(name = "fee_per_kwu_usd")
    val feePerKwuUsd: String,
    @field:Json(name = "fee_usd")
    val feeUsd: String,
    @field:Json(name = "has_witness")
    val hasWitness: Boolean?,
    @field:Json(name = "hash")
    val hash: String,
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "input_count")
    val inputCount: Int,
    @field:Json(name = "input_total")
    val inputTotal: String,
    @field:Json(name = "input_total_usd")
    val inputTotalUsd: String,
    @field:Json(name = "is_coinbase")
    val isCoinbase: Boolean,
    @field:Json(name = "lock_time")
    val lockTime: Int,
    @field:Json(name = "output_count")
    val outputCount: Int,
    @field:Json(name = "output_total")
    val outputTotal: String,
    @field:Json(name = "output_total_usd")
    val outputTotalUsd: String,
    @field:Json(name = "size")
    val size: Int,
    @field:Json(name = "time")
    val time: String,
    @field:Json(name = "version")
    val version: Int,
    @field:Json(name = "weight")
    val weight: Int
)
