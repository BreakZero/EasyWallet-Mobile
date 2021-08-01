package com.easy.wallet.feature.settings.chain

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.feature.settings.chain.adapter.WrapChain
import com.easy.wallet.feature.settings.uimodel.ChainState
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.actionOn
import io.uniflow.core.flow.data.UIEvent
import org.koin.core.component.KoinComponent

class SetChainViewModel : AndroidDataFlow(), KoinComponent {
    private var chains: List<WrapChain>

    init {
        chains = listOf(
            ChainId.MAINNET,
            ChainId.ROPSTEN,
            ChainId.RINKEBY,
            ChainId.GÖRLI,
            ChainId.KOVAN
        ).map {
            WrapChain(
                name = it.name,
                checked = it.name == WalletDataSDK.chainId().name
            )
        }
        action {
            setState { ChainState(chains) }
        }
    }

    fun updateChain(name: String) = actionOn<ChainState> {
        setState {
            chains = chains.map {
                WrapChain(
                    name = it.name,
                    checked = it.name == name
                )
            }
            ChainState(chains)
        }
    }

    fun done() = actionOn<ChainState> {
        val name = chains.find { it.checked }?.name.orEmpty()
        WalletDataSDK.updateChain(name)
        sendEvent(UIEvent.Success)
    }
}
