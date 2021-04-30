package com.dougie.wallet.feature.nft.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.dougie.wallet.data.data.model.nft.NFTCollectionDataModel

class NFTCollectionController(
    private val itemListener: (NFTCollectionDataModel) -> Unit
) : TypedEpoxyController<List<NFTCollectionDataModel>>() {
    override fun buildModels(data: List<NFTCollectionDataModel>?) {
        data?.let {
            it.forEach { item ->
                nFTCollection {
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
