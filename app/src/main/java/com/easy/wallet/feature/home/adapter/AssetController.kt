package com.easy.wallet.feature.home.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.easy.wallet.data.Asset

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
          this@AssetController.onItemClick.invoke(asset)
        }
        onReceive {
          this@AssetController.onReceive.invoke(it)
        }
      }
    }
  }
}
