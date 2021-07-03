package com.easy.wallet.data.data.remote.opensea

import com.squareup.moshi.Json

internal data class NFTTrait(
    @field:Json(name = "trait_count")
    val traitCount: Int,
    @field:Json(name = "trait_type")
    val traitType: String,
    @field:Json(name = "value")
    val value: String
)
