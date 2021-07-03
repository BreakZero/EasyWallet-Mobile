package com.easy.wallet.data.data.remote.cosmos.request

import com.squareup.moshi.Json

data class CosmosTxRequest(
    @field:Json(name = "account")
    val account: List<String>,
    @field:Json(name = "chain_ids")
    val chainIds: List<String>,
    @field:Json(name = "limit")
    val limit: Int,
    @field:Json(name = "network")
    val network: String,
    @field:Json(name = "type")
    val type: List<String>? = null
)
