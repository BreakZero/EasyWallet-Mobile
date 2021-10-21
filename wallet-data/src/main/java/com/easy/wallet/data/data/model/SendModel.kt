package com.easy.wallet.data.data.model

import java.math.BigInteger

data class SendModel(
  val amount: BigInteger,
  val feeByte: Float,
  val memo: String = "",
  val to: String,
  val useMax: Boolean = false,
  val txModel: TxModel = TxModel.Legacy
)

enum class TxModel {
  Legacy, Enveloped
}
