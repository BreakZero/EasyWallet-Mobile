package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

internal data class BalanceResponse(
    @field:Json(name = "height")
    val height: String,
    @field:Json(name = "result")
    val result: List<AtomBalance>
)

internal data class ValidatorResponse(
    @field:Json(name = "height")
    val height: String,
    @field:Json(name = "result")
    val result: List<ValidatorInfo>
)

internal data class BroadcastResponse(
    @field:Json(name = "check_tx")
    val checkTx: TxResult,
    @field:Json(name = "deliver_tx")
    val deliverTx: TxResult,
    @field:Json(name = "txhash")
    val hash: String,
    @field:Json(name = "height")
    val height: Int
)

internal data class AccountInfo(
    @field:Json(name = "height")
    val height: String,
    @field:Json(name = "result")
    val result: AccountResult
)
