package com.easy.wallet.data.data.remote.dot

import com.squareup.moshi.Json

internal data class DOTFeeResult(
    @field:Json(name = "class")
    val classX: String,
    @field:Json(name = "partialFee")
    val partialFee: String,
    @field:Json(name = "weight")
    val weight: Int
)
