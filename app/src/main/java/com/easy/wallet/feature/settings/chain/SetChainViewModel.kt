package com.easy.wallet.feature.settings.chain

import com.dougie.framework.base.BaseViewModel
import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.data.constant.ChainId
import com.dougie.wallet.feature.settings.chain.adapter.WrapChain
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class SetChainViewModel : BaseViewModel(), KoinComponent {
    private val _chains = MutableStateFlow(
        listOf(
            ChainId.MAINNET,
            ChainId.ROPSTEN,
            ChainId.RINKEBY,
            ChainId.GÖRLI,
            ChainId.KOVAN
        ).map {
            WrapChain(
                name = it.name,
                checked = it.name == DeFiWalletSDK.chainId().name
            )
        }
    )

    fun chains() = _chains

    fun updateState(name: String) {
        _chains.value = _chains.value.map {
            WrapChain(
                name = it.name,
                checked = it.name == name
            )
        }
    }

    fun updateChain() {
        val name = _chains.value.find { it.checked }?.name.orEmpty()
        DeFiWalletSDK.updateChain(name)
    }
}
