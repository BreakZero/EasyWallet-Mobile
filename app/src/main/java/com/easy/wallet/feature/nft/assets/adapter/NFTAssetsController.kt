package com.easy.wallet.feature.nft.assets.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.easy.wallet.data.data.model.nft.NFTAssetDataModel

class NFTAssetsController(
  private val itemListener: (NFTAssetDataModel) -> Unit
) : TypedEpoxyController<List<NFTAssetDataModel>>() {
  override fun buildModels(data: List<NFTAssetDataModel>?) {
    data?.let {
      it.forEach { item ->
        nFTAsset {
          infoData(item)
          id(item.hashCode())
          onItemClick {
            itemListener.invoke(item)
          }
        }
      }
    }
  }
}
