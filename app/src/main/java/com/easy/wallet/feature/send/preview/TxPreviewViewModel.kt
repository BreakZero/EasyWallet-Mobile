package com.easy.wallet.feature.send.preview

import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.feature.send.uimodel.SendPreviewEvent
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState

class TxPreviewViewModel(
  currencyInfo: CurrencyInfo
) : AndroidDataFlow() {
  private val coinProvider =
    WalletDataSDK.injectProvider(currencyInfo.slug, currencyInfo.symbol, currencyInfo.decimal)

  fun broadcastTransaction(rawData: String) = action {
    sendEvent(UIEvent.Loading)
    val flow = coinProvider.broadcastTransaction(rawData)
    onFlow(
      flow = { flow },
      doAction = {
        setState { UIState.Success }
      },
      onError = { error, _ ->
        setState { UIState.Failed(error = error) }
      }
    )
  }

  fun actionBroadcast() = action {
    sendEvent(SendPreviewEvent.EventBroadcast)
  }
}
