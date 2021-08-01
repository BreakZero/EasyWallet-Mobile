package com.easy.wallet.feature.nft

import androidx.lifecycle.viewModelScope
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.nft.NFTCollectionDataModel
import io.uniflow.android.AndroidDataFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NFTCollectionsViewModel : AndroidDataFlow() {
    private val nftManager = WalletDataSDK.injectNFTManager()

    private val _state = MutableStateFlow(
        NFTCollectState(
            loading = true,
            listOf()
        )
    )

    private fun currState() = _state.value

    fun observeState() = _state

    fun loadCollections() {
        nftManager.loadCollections()
            .onEach {
                _state.value = currState().copy(
                    loading = false,
                    data = it
                )
            }.launchIn(viewModelScope)
    }
}

data class NFTCollectState(
    val loading: Boolean = true,
    val data: List<NFTCollectionDataModel>
)
