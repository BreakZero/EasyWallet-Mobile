package com.easy.wallet.feature.nft.asset

import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.nft.NFTAssetDataModel
import com.easy.wallet.data.param.NFTAssetParameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class NFTAssetViewModel : BaseViewModel() {
    private val _asset = MutableStateFlow(NFTAssetDataModel.EMPTY)

    private val nftManager = WalletDataSDK.injectNFTManager()

    fun asset() = _asset

    fun loadAssetDetail(asset: NFTAssetParameter) {
        nftManager.getAssetDetail(
            asset.contractAddress,
            asset.tokenId
        ).onEach {
            _asset.value = it
            Timber.d("nft assets: $it")
        }.launchIn(viewModelScope)
    }

    fun send() {
        if (_asset.value.isEmpty()) return
        nftManager.buildSendModel(
            _asset.value,
            "0xE82bc6A5364D16D23645054cda1e694F8B69f688",
            "120".toBigInteger()
        ).flatMapConcat {
            nftManager.broadcastTransaction(it.rawData)
        }.onEach {
            Timber.d("nft transaction hash: $it")
        }.launchIn(viewModelScope)
    }
}
