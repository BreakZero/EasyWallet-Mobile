package com.dougie.wallet.feature.settings.chain.adapter

import com.airbnb.epoxy.TypedEpoxyController

class ChainsController(private val checkedChange: (String) -> Unit) :
    TypedEpoxyController<List<WrapChain>>() {
    override fun buildModels(data: List<WrapChain>?) {
        data?.forEach { entity ->
            chains {
                id(entity.name)
                item(entity)
                onCheckChanged {
                    checkedChange.invoke(entity.name)
                }
            }
        }
    }
}
