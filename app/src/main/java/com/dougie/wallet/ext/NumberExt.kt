package com.dougie.wallet.ext

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

private val numberFormat = NumberFormat.getNumberInstance()
fun BigDecimal.upDecimal(decimal: Int, scale: Int = 8): BigDecimal {
    return movePointRight(decimal).setScale(scale, RoundingMode.HALF_UP)
        .stripTrailingZeros()
}

fun BigDecimal.downDecimal(decimal: Int, scale: Int = 8): BigDecimal {
    return movePointLeft(decimal).setScale(scale, RoundingMode.HALF_UP)
        .stripTrailingZeros()
}

fun BigDecimal.toCoinAmount(digits: Int = 2): String {
    numberFormat.minimumFractionDigits = digits
    return numberFormat.format(this)
}

fun BigDecimal.strByDecimal(decimal: Int = 0, scale: Int = 8): String {
    return downDecimal(decimal, scale).toPlainString()
}

fun BigDecimal.byDownDecimal(decimal: Int, digits: Int = 2): String {
    numberFormat.minimumFractionDigits = digits
    return numberFormat.format(this.movePointLeft(decimal))
}

fun BigDecimal.byUpDecimal(decimal: Int, digits: Int = 2): String {
    numberFormat.minimumFractionDigits = digits
    return numberFormat.format(this.movePointRight(decimal))
}

fun BigDecimal.toFiatCurrency(currencyCode: String = "USD"): String {
    numberFormat.minimumFractionDigits = 2
    numberFormat.currency = Currency.getInstance(currencyCode)
    return numberFormat.format(this.toDouble())
}
