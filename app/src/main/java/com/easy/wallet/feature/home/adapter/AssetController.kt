package com.easy.wallet.feature.home.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.dougie.wallet.data.Asset

class AssetController(
    private val onItemClick: (asset: Asset) -> Unit,
    private val onReceive: (address: String) -> Unit
) : TypedEpoxyController<List<Asset>>() {
    override fun buildModels(data: List<Asset>?) {
        data?.forEach { asset ->
            asset {
                id(asset.coinInfo.hashCode())
                assetData(asset)
                onItemClick {
                    onItemClick.invoke(asset)
                }
                onReceive {
                    onReceive.invoke(it)
                }
            }
        }
    }
}
