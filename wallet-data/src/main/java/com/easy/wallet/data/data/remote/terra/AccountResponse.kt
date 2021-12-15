package com.easy.wallet.data.data.remote.terra

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal data class AccountResponse(
    @field:Json(name = "account")
    val account: Account
)