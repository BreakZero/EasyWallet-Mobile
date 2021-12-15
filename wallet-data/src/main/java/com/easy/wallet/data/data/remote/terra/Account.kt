package com.easy.wallet.data.data.remote.terra

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal data class Account(
    @Json(name = "account_number")
    val accountNumber: String,
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "sequence")
    val sequence: String
)