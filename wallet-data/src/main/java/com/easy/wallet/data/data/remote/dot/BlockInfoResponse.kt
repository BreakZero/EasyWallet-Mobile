package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

internal data class Blocks(
    @field:Json(name = "blocks")
    val blocks: List<BlockInfo>
)

internal data class BlockInfo(
    @field:Json(name = "account_display")
    val accountDisplay: AccountDisplay,
    @field:Json(name = "block_num")
    val blockNum: Int,
    @field:Json(name = "block_timestamp")
    val blockTimestamp: Int,
    @field:Json(name = "event_count")
    val eventCount: Int,
    @field:Json(name = "extrinsics_count")
    val extrinsicsCount: Int,
    @field:Json(name = "finalized")
    val finalized: Boolean,
    @field:Json(name = "hash")
    val hash: String,
    @field:Json(name = "validator")
    val validator: String,
    @field:Json(name = "validator_index_ids")
    val validatorIndexIds: String,
    @field:Json(name = "validator_name")
    val validatorName: String
)

internal data class AccountDisplay(
    @field:Json(name = "account_index")
    val accountIndex: String,
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "display")
    val display: String,
    @field:Json(name = "identity")
    val identity: Boolean,
    @field:Json(name = "judgements")
    val judgements: Any,
    @field:Json(name = "parent")
    val parent: Parent
)

internal data class Parent(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "display")
    val display: String,
    @field:Json(name = "identity")
    val identity: Boolean,
    @field:Json(name = "sub_symbol")
    val subSymbol: String
)
