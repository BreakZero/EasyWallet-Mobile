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
              "https://cryptologos.cc/logos/uniswap-uni-logo.png",
              "Uni Swap",
              "https://app.uniswap.org/#/swap",
              3,
              WalletDataSDK.dAppRPC(3)
            ),
            DAppInfo(
              "https://img2.baidu.com/it/u=3117495242,168525747&fm=26&fmt=auto&gp=0.jpg",
              "Pancake swap",
              "https://pancakeswap.finance/swap",
              56,
              WalletDataSDK.dAppRPC(56)
            ),
            DAppInfo(
              "https://img2.baidu.com/it/u=3117495242,168525747&fm=26&fmt=auto&gp=0.jpg",
              "Simple Link",
              "https://js-eth-sign.surge.sh",
              56,
              "https://bsc-dataseed2.binance.org"
            ),
            DAppInfo(
              "https://img2.baidu.com/it/u=3117495242,168525747&fm=26&fmt=auto&gp=0.jpg",
              "Simple Link",
              "https://pancakeswap.finance/swap#/swap",
              56,
              "https://bsc-dataseed.binance.org"
            )
          )
        )
      }
    }
  }
}
