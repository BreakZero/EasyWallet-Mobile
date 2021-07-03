package com.dougie.wallet.data

import android.os.Parcelable
import comdougiewalletdata.CoinConfig
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyInfo(
    val slug: String,
    val symbol: String,
    val name: String,
    val decimal: Int,
    val displayDecimal: Int,
    val accentColor: String,
    val feeMin: Int,
    val feeMax: Int
) : Parcelable {
    companion object {
        fun mapping(config: CoinConfig): CurrencyInfo {
            return CurrencyInfo(
                slug = config.coin_slug,
                symbol = config.coin_symbol,
                name = config.coin_name,
                decimal = config.coin_decimal,
                displayDecimal = config.display_decimal,
                accentColor = config.accent_color,
                feeMin = 20,
                feeMax = 1000
            )
        }
    }
}
