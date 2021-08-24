package com.easy.wallet.data.data.remote.blockchair.btc

import com.easy.wallet.data.data.remote.CoinContext
import com.squareup.moshi.Json

internal data class BitcoinRelatedInfoResponse(
  @field:Json(name = "data")
  val data: Map<String, BitcoinRelatedInfo>,
  @field:Json(name = "context")
  val context: CoinContext
)

internal data class BitcoinRelatedInfo(
  @field:Json(name = "address")
  val addressInfo: AddressRelatedInfo,
  @field:Json(name = "transactions")
  val transactionsHash: List<String>,
  @field:Json(name = "utxo")
  val utxos: List<UTXOInfo>
)

internal data class AddressRelatedInfo(
  @field:Json(name = "balance")
  val balance: String,
  @field:Json(name = "balance_usd")
  val balanceUsd: String,
  @field:Json(name = "first_seen_receiving")
  val firstSeenReceiving: String,
  @field:Json(name = "first_seen_spending")
  val firstSeenSpending: String,
  @field:Json(name = "last_seen_receiving")
  val lastSeenReceiving: String,
  @field:Json(name = "last_seen_spending")
  val lastSeenSpending: String,
  @field:Json(name = "output_count")
  val outputCount: Int,
  @field:Json(name = "received")
  val received: Long,
  @field:Json(name = "received_usd")
  val receivedUsd: String,
  @field:Json(name = "script_hex")
  val scriptHex: String,
  @field:Json(name = "spent")
  val spent: Long,
  @field:Json(name = "spent_usd")
  val spentUsd: String,
  @field:Json(name = "transaction_count")
  val transactionCount: Int?,
  @field:Json(name = "type")
  val type: String,
  @field:Json(name = "unspent_output_count")
  val unspentOutputCount: Int
)

internal data class UTXOInfo(
  @field:Json(name = "block_id")
  val blockId: Long,
  @field:Json(name = "index")
  val index: Int,
  @field:Json(name = "transaction_hash")
  val transactionHash: String,
  @field:Json(name = "value")
  val value: Long
)
