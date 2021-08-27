package com.easy.wallet.feature.defi

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.feature.defi.uimodels.DAppInfo
import com.easy.wallet.feature.defi.uimodels.DeFiListState
import io.uniflow.android.AndroidDataFlow

class DefiMainViewModel : AndroidDataFlow() {
  init {
    action {
      setState {
        DeFiListState(
          listOf(
            DAppInfo(
              "https://cryptologos.cc/logos/uniswap-uni-logo.png",
              "Uni Swap",
              "https://app.uniswap.org/#/swap",
              1,
              WalletDataSDK.dAppRPC(1)
            ),
            DAppInfo(
              "https://gblobscdn.gitbook.com/spaces%2F-MHREX7DHcljbY5IkjgJ%2Favatar-1602750187173.png",
              "Pancake swap",
              "https://pancakeswap.finance/swap",
              56,
              WalletDataSDK.dAppRPC(56)
            )
          )
        )
      }
    }
  }
}
