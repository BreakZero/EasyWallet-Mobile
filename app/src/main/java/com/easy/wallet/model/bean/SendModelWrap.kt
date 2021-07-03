package com.easy.wallet.model.bean

import android.os.Parcelable
import com.dougie.wallet.ext.strByDecimal
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.BigInteger

@Parcelize
data class SendModelWrap(
    val gasLimit: BigInteger,
    val gas: BigDecimal,
    val to: String,
    val from: String,
    val amount: BigDecimal,
    val symbol: String,
    val feeDecimals: Int,
    val memo: String,
    val rawData: String
) : Parcelable {
    fun feeWithSymbol(): String {
        return "${gasLimit.toBigDecimal().times(gas).movePointLeft(feeDecimals).strByDecimal()} $symbol"
    }
}
