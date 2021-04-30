package com.dougie.wallet.data.data.model

data class DAppSendModel(
    val from: String,
    val to: String,
    val gasPrice: String,
    val gasLimit: String,
    val value: String,
    val data: String
)
