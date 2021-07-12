package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

internal data class BasicInfoResult(
    @field:Json(name = "authoringVersion")
    val authoringVersion: Int,
    @field:Json(name = "implName")
    val implName: String,
    @field:Json(name = "implVersion")
    val implVersion: Int,
    @field:Json(name = "specName")
    val specName: String,
    @field:Json(name = "specVersion")
    val specVersion: Int,
    @field:Json(name = "transactionVersion")
    val transactionVersion: Int
)
