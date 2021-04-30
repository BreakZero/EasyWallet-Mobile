package com.dougie.wallet.data.data.model

import java.math.BigInteger

data class SendModel(
    val amount: BigInteger,
    val feeByte: Float,
    val memo: String = "",
    val to: String,
    val useMax: Boolean = false
)
