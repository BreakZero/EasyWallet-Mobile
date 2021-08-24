package com.easy.wallet.data.data.model

import java.math.BigDecimal
import java.math.BigInteger

data class SendPlanModel(
  val amount: BigDecimal = BigDecimal.ZERO,
  val toAddress: String = "",
  val fromAddress: String = "",
  val gasLimit: BigInteger = BigInteger.ONE,
  val gas: BigDecimal,
  val feeDecimals: Int,
  val memo: String = "",
  val rawData: String = ""
)
