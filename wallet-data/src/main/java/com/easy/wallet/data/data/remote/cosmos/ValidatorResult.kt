package com.easy.wallet.data.data.remote.cosmos

import com.squareup.moshi.Json

internal data class ValidatorInfo(
    @field:Json(name = "bond_height")
    val bondHeight: String,
    @field:Json(name = "bond_intra_tx_counter")
    val bondIntraTxCounter: Int,
    @field:Json(name = "commission")
    val commission: Commission,
    @field:Json(name = "consensus_pubkey")
    val consensusPubkey: String,
    @field:Json(name = "delegator_shares")
    val delegatorShares: String,
    @field:Json(name = "description")
    val description: Description,
    @field:Json(name = "jailed")
    val jailed: Boolean,
    @field:Json(name = "operator_address")
    val operatorAddress: String,
    @field:Json(name = "status")
    val status: Int,
    @field:Json(name = "tokens")
    val tokens: String,
    @field:Json(name = "unbonding_height")
    val unbondingHeight: String,
    @field:Json(name = "unbonding_time")
    val unbondingTime: String
)

internal data class Commission(
    @field:Json(name = "max_change_rate")
    val maxChangeRate: String,
    @field:Json(name = "max_rate")
    val maxRate: String,
    @field:Json(name = "rate")
    val rate: String,
    @field:Json(name = "update_time")
    val updateTime: String
)

internal data class Description(
    @field:Json(name = "details")
    val details: String,
    @field:Json(name = "identity")
    val identity: String,
    @field:Json(name = "moniker")
    val moniker: String,
    @field:Json(name = "website")
    val website: String
)
