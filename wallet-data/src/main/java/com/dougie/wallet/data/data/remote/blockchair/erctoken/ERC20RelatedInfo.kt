package com.dougie.wallet.data.data.remote.blockchair.erctoken

import com.dougie.wallet.data.data.remote.CoinContext
import com.squareup.moshi.Json

internal data class TokenRelatedInfoResponse(
    @field:Json(name = "data")
    val data: Map<String, TokenRelateInfo>,
    @field:Json(name = "context")
    val context: CoinContext
)

internal data class TokenRelateInfo(
    @field:Json(name = "address")
    val address: TokenAddressRelateInfo,
    @field:Json(name = "transactions")
    val transactions: List<TokenTxInfo>
)

internal data class TokenAddressRelateInfo(
    @field:Json(name = "balance")
    val balance: String,
    @field:Json(name = "balance_approximate")
    val balanceApproximate: String,
    @field:Json(name = "first_seen_receiving")
    val firstSeenReceiving: String,
    @field:Json(name = "first_seen_spending")
    val firstSeenSpending: Any,
    @field:Json(name = "last_seen_receiving")
    val lastSeenReceiving: String,
    @field:Json(name = "last_seen_spending")
    val lastSeenSpending: Any,
    @field:Json(name = "received")
    val received: String,
    @field:Json(name = "received_approximate")
    val receivedApproximate: String,
    @field:Json(name = "receiving_transaction_count")
    val receivingTransactionCount: Int,
    @field:Json(name = "spending_transaction_count")
    val spendingTransactionCount: Int,
    @field:Json(name = "spent")
    val spent: String,
    @field:Json(name = "spent_approximate")
    val spentApproximate: String,
    @field:Json(name = "transaction_count")
    val transactionCount: Int
)

internal data class TokenTxInfo(
    @field:Json(name = "block_id")
    val blockId: Long,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "recipient")
    val recipient: String,
    @field:Json(name = "sender")
    val sender: String,
    @field:Json(name = "time")
    val time: String,
    @field:Json(name = "token_address")
    val tokenAddress: String,
    @field:Json(name = "token_decimals")
    val tokenDecimals: Int,
    @field:Json(name = "token_name")
    val tokenName: String,
    @field:Json(name = "token_symbol")
    val tokenSymbol: String,
    @field:Json(name = "transaction_hash")
    val transactionHash: String,
    @field:Json(name = "value")
    val value: String,
    @field:Json(name = "value_approximate")
    val valueApproximate: String
)
