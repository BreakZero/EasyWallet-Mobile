package com.easy.wallet.feature.coin

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.feature.coin.uimodel.SetCoinUIEvent
import com.easy.wallet.feature.coin.uimodel.SupportCoinState
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.data.UIState
import io.uniflow.core.threading.onIO
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CoinListViewModel : AndroidDataFlow() {
    init {
        action(
            onAction = {
                setState { SupportCoinState(WalletDataSDK.allAssets()) }
            },
            onError = { error, _ ->
                setState { UIState.Failed("load coins failed", error) }
            }
        )
    }

    private val changeHolder = hashMapOf<String, Int>()

    fun onChange(slug: String) = action {
        changeHolder[slug]?.plus(1) ?: kotlin.run { changeHolder[slug] = 1 }
        sendEvent(SetCoinUIEvent.UpdateState)
    }

    @ExperimentalCoroutinesApi
    fun done() = action {
        val result = changeHolder.filter { it.value % 2 == 1 }
        onIO {
            result.map { it.key }.forEach {
                WalletDataSDK.toggleBySlug(it)
            }
        }
        sendEvent(SetCoinUIEvent.DoneEvent(result.isNotEmpty()))
    }
}
