package com.easy.wallet.feature.coin.adapter

import com.airbnb.epoxy.TypedEpoxyController
import comdougiewalletdata.CoinConfig

class CoinController(private val checkedChange: (String) -> Unit) :
    TypedEpoxyController<List<CoinConfig>>() {
    override fun buildModels(data: List<CoinConfig>?) {
        data?.forEach { entity ->
            coin {
                id(entity.coin_symbol)
                check(entity.is_active)
                coinName(entity.coin_name)
                onCheckChanged {
                    checkedChange.invoke(entity.coin_symbol)
                }
            }
        }
    }
}
