package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

data class TransactionResponse(
  @field:Json(name = "block_hash")
  val blockHash: String,
  @field:Json(name = "chain_id")
  val chainId: String,
  @field:Json(name = "gas_used")
  val gasUsed: Int,
  @field:Json(name = "gas_wanted")
  val gasWanted: Int,
  @field:Json(name = "has_errors")
  val hasErrors: Boolean,
  @field:Json(name = "hash")
  val hash: String,
  @field:Json(name = "height")
  val height: Int,
  @field:Json(name = "id")
  val id: String,
  @field:Json(name = "time")
  val time: String,
  @field:Json(name = "version")
  val version: String,
  @field:Json(name = "transaction_fee")
  val txFees: List<TransactionFee>,
  @field:Json(name = "events")
  val events: List<TransactionEvent>
)

data class TransactionFee(
  @field:Json(name = "currency")
  val currency: String,
  @field:Json(name = "numeric")
  val numeric: Int,
  @field:Json(name = "text")
  val text: String
)

data class TransactionEvent(
  @field:Json(name = "id")
  val id: String,
  @field:Json(name = "kind")
  val kind: String,
  @field:Json(name = "sub")
  val sub: List<SubInfo>
)

data class Account(
  @field:Json(name = "id")
  val id: String
)

data class Amount(
  @field:Json(name = "currency")
  val currency: String,
  @field:Json(name = "numeric")
  val numeric: Int,
  @field:Json(name = "text")
  val text: String
)

data class Recipient(
  @field:Json(name = "account")
  val account: Account
)

data class Reward(
  @field:Json(name = "account")
  val account: Account,
  @field:Json(name = "amounts")
  val amounts: List<Amount>
)

data class SubInfo(
  @field:Json(name = "module")
  val module: String,
  @field:Json(name = "node")
  val node: Node,
  @field:Json(name = "recipient")
  val recipient: List<Recipient>,
  @field:Json(name = "transfers")
  val transfers: Transfers?,
  @field:Json(name = "type")
  val type: List<String>
)

data class Transfers(
  @field:Json(name = "reward")
  val reward: List<Reward>
)

data class Node(
  @field:Json(name = "delegator")
  val delegator: List<Account>,
  @field:Json(name = "validator")
  val validator: List<Account>
)
