package com.easy.wallet.feature.send.uimodel

import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.model.bean.SendModelWrap
import io.uniflow.core.flow.data.UIState

sealed class SendStates : UIState() {
  data class InfoState(
    val currency: CurrencyInfo,
    val balance: String
  ) : SendStates()

  data class BalanceFailedState(
    val currency: CurrencyInfo
  ) : SendStates()

  data class BuildState(
    val sendModel: SendModelWrap
  ) : SendStates()
}
