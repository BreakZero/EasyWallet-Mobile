package com.easy.wallet.data.data.remote.testnet

import com.squareup.moshi.Json

data class StateResponse(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "data")
    val info: DataInfo,
    @field:Json(name = "time")
    val time: Double
)

data class DataInfo(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "balance")
    val balance: Long,
    @field:Json(name = "callCount")
    val callCount: Int,
    @field:Json(name = "firstReceivedTxBlock")
    val firstReceivedTxBlock: Int,
    @field:Json(name = "internalCount")
    val internalCount: Int,
    @field:Json(name = "invalidTxCount")
    val invalidTxCount: Int,
    @field:Json(name = "is_contract")
    val isContract: Boolean,
    @field:Json(name = "largestTxAmount")
    val largestTxAmount: Long,
    @field:Json(name = "largestTxBlock")
    val largestTxBlock: Int,
    @field:Json(name = "lastReceivedTxBlock")
    val lastReceivedTxBlock: Int,
    @field:Json(name = "minedBlocksAmount")
    val minedBlocksAmount: Int,
    @field:Json(name = "minedBlocksCount")
    val minedBlocksCount: Int,
    @field:Json(name = "minedUnclesAmount")
    val minedUnclesAmount: Int,
    @field:Json(name = "minedUnclesCount")
    val minedUnclesCount: Int,
    @field:Json(name = "pendingReceivedTxCount")
    val pendingReceivedTxCount: Int,
    @field:Json(name = "pendingSentTxCount")
    val pendingSentTxCount: Int,
    @field:Json(name = "receivedAmount")
    val receivedAmount: Long,
    @field:Json(name = "receivedTxCount")
    val receivedTxCount: Int,
    @field:Json(name = "sentAmount")
    val sentAmount: Int,
    @field:Json(name = "sentTxCount")
    val sentTxCount: Int,
    @field:Json(name = "transferCount")
    val transferCount: Int
)
