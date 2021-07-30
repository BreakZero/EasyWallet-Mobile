package com.easy.wallet.feature.coin.uimodel

import comeasywalletdata.CoinConfig
import io.uniflow.core.flow.data.UIState

data class SupportCoinState(
    val list: List<CoinConfig>
) : UIState()
