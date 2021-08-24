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
            DAppInfo("https://pbs.twimg.com/profile_images/1155018549510852610/ioEfq7r4.jpg", "Uni Swap", "https://app.uniswap.org/#/swap"),
            DAppInfo("https://www.chaoniu520.com/content/top15img/202010//65e7f6fa739d863acc79dfc4d6141733.jpg", "DeFi Swap", "https://crypto.com/defi/swap/"),
          )
        )
      }
    }
  }
}
