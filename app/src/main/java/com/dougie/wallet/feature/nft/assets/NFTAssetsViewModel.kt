package com.dougie.wallet.feature.nft.assets

import androidx.lifecycle.viewModelScope
import com.dougie.framework.base.BaseViewModel
import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.data.data.model.nft.NFTAssetDataModel
import com.dougie.wallet.data.data.model.nft.NFTCollectionDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class NFTAssetsViewModel : BaseViewModel() {
    private val _assets = MutableStateFlow<List<NFTAssetDataModel>>(listOf())

    private val nftManager = DeFiWalletSDK.injectNFTManager()

    fun assets() = _assets

    fun loadAssets(collection: NFTCollectionDataModel) {
        nftManager.loadAssets(
            collection.contractAddress,
            collection.slug
        ).onEach {
            _assets.value = it
            Timber.d("nft assets: $it")
        }.launchIn(viewModelScope)
    }

    fun getAssetDetail(asset: NFTAssetDataModel) {
        nftManager.getAssetDetail(
            asset.contractAddress,
            asset.tokenId
        )
    }
}
