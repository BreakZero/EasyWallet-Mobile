package com.easy.wallet.data.data.remote

import com.squareup.moshi.Json

internal data class CoinContext(
    @field:Json(name = "api")
    val api: CoinApi,
    @field:Json(name = "cache")
    val cache: CoinCache,
    @field:Json(name = "code")
    val code: Int,
    @field:Json(name = "limit")
    val limit: String,
    @field:Json(name = "offset")
    val offset: String,
    @field:Json(name = "results")
    val results: Int,
    @field:Json(name = "source")
    val source: String,
    @field:Json(name = "state")
    val state: Int,
    @field:Json(name = "state_layer_2")
    val stateLayer2: Int,
    @field:Json(name = "time")
    val time: Double,
    @field:Json(name = "error")
    val error: String? = null
)

internal data class CoinCache(
    @field:Json(name = "duration")
    val duration: String,
    @field:Json(name = "live")
    val live: Boolean,
    @field:Json(name = "since")
    val since: String,
    @field:Json(name = "time")
    val time: String?,
    @field:Json(name = "until")
    val until: String
)

internal data class CoinApi(
    @field:Json(name = "documentation")
    val documentation: String,
    @field:Json(name = "last_major_update")
    val lastMajorUpdate: String,
    @field:Json(name = "next_major_update")
    val nextMajorUpdate: String = "",
    @field:Json(name = "notice")
    val notice: String,
    @field:Json(name = "version")
    val version: String
)

internal data class BaseBlockChairResponse<out T>(
    @field:Json(name = "data")
    val data: T,
    @field:Json(name = "context")
    val context: CoinContext
)
