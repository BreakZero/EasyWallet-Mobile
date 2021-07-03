package com.easy.wallet.data.data.remote.blockchair.ethereum

import com.dougie.wallet.data.data.remote.CoinContext
import com.squareup.moshi.Json

internal data class EthereumRelatedInfoResponse(
    @field:Json(name = "data")
    val result: Map<String, EthereumRelatedInfo>,
    @field:Json(name = "context")
    val context: CoinContext
)

internal data class EthereumRelatedInfo(
    @field:Json(name = "address")
    val address: EthereumAddressInfo,
    @field:Json(name = "calls")
    val calls: List<EthereumCallInfo>
)

internal data class EthereumAddressInfo(
    @field:Json(name = "balance")
    val balance: String?,
    @field:Json(name = "balance_usd")
    val balanceUsd: Double,
    @field:Json(name = "call_count")
    val callCount: Int,
    @field:Json(name = "contract_code_hex")
    val contractCodeHex: String?,
    @field:Json(name = "contract_created")
    val contractCreated: Boolean?,
    @field:Json(name = "contract_destroyed")
    val contractDestroyed: Boolean?,
    @field:Json(name = "fees_approximate")
    val feesApproximate: String,
    @field:Json(name = "fees_usd")
    val feesUsd: String,
    @field:Json(name = "first_seen_receiving")
    val firstSeenReceiving: String,
    @field:Json(name = "first_seen_spending")
    val firstSeenSpending: String,
    @field:Json(name = "last_seen_receiving")
    val lastSeenReceiving: String,
    @field:Json(name = "last_seen_spending")
    val lastSeenSpending: String,
    @field:Json(name = "nonce")
    val nonce: Int?,
    @field:Json(name = "received_approximate")
    val receivedApproximate: String,
    @field:Json(name = "received_usd")
    val receivedUsd: String,
    @field:Json(name = "receiving_call_count")
    val receivingCallCount: Int,
    @field:Json(name = "spending_call_count")
    val spendingCallCount: Int,
    @field:Json(name = "spent_approximate")
    val spentApproximate: String,
    @field:Json(name = "spent_usd")
    val spentUsd: String,
    @field:Json(name = "transaction_count")
    val transactionCount: Int,
    @field:Json(name = "type")
    val type: String
)

internal data class EthereumCallInfo(
    @field:Json(name = "block_id")
    val blockId: Long,
    @field:Json(name = "index")
    val index: String?,
    @field:Json(name = "recipient")
    val recipient: String,
    @field:Json(name = "sender")
    val sender: String,
    @field:Json(name = "time")
    val time: String,
    @field:Json(name = "transaction_hash")
    val transactionHash: String?,
    @field:Json(name = "transferred")
    val transferred: Boolean?,
    @field:Json(name = "values")
    val value: String,
    @field:Json(name = "value_usd")
    val valueUsd: String
)
