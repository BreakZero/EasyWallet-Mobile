package com.easy.wallet.feature.defi

import com.easy.wallet.feature.defi.uimodels.DAppInfo
import com.easy.wallet.feature.defi.uimodels.DeFiListState
import io.uniflow.android.AndroidDataFlow

class DefiMainViewModel : AndroidDataFlow() {
  init {
    action {
      setState {
        DeFiListState(
          listOf(
            DAppInfo("https://app.uniswap.org/static/media/logo.4a50b488.svg", "Uni Swap", "https://app.uniswap.org/#/swap"),
            DAppInfo("https://crypto.com/static/cae522dc778b83946d0bbe0ee927090c/8d5b4/white.png", "DeFi Swap", "https://crypto.com/defi/swap/"),
          )
        )
      }
    }
  }
}
