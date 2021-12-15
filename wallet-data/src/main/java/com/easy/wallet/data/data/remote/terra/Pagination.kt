package com.easy.wallet.data.data.remote.terra

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
internal data class Pagination(
    @field:Json(name = "total")
    val total: String
)